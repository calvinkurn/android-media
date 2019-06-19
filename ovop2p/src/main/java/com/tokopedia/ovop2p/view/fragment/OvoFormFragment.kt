package com.tokopedia.ovop2p

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.model.WalletDataBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.activity.AllContactsActivity
import com.tokopedia.ovop2p.view.activity.OvoP2pWebViewActivity
import com.tokopedia.ovop2p.view.adapters.ContactsCursorAdapter
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener
import com.tokopedia.ovop2p.viewmodel.GetWalletBalanceViewModel
import com.tokopedia.ovop2p.viewmodel.OvoP2pTransferRequestViewModel
import com.tokopedia.ovop2p.viewmodel.OvoP2pTrxnConfirmVM
import kotlinx.android.synthetic.main.ovo_p2p_transfer_form.*

class OvoFormFragment : BaseDaggerFragment(), View.OnClickListener, View.OnFocusChangeListener, SearchView.OnQueryTextListener {

    lateinit var searchView: SearchView
    lateinit var saldoTextView: TextView
    lateinit var trnsfrAmtEdtxtv: EditText
    lateinit var msgEdtxtv: EditText
    lateinit var proceedBtn: Button
    lateinit var contactsImageView: ImageView
    lateinit var searchNoHeader: TextView
    lateinit var walletBalanceViewModel: GetWalletBalanceViewModel
    lateinit var alertDialog: AlertDialog
    lateinit var ovoP2pTransferRequestViewModel: OvoP2pTransferRequestViewModel
    lateinit var ovoP2pTransferConfirmViewModel: OvoP2pTrxnConfirmVM
    lateinit var trnsfrReqDataMap: HashMap<String, Any>
    private var rcvrPhnNo: String = ""
    private var rcvrAmt: Int = 0
    private var rcvrMsg: String = ""

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if(id != -1){
            when(id){
                R.id.proceed -> {
                    //make request call
                    (activity as LoaderUiListener).showProgressDialog()
                    context?.let { ovoP2pTransferRequestViewModel.makeTransferRequestCall(it, trnsfrReqDataMap) }
                }
                R.id.iv_contact -> {
                    val intent = Intent(activity, AllContactsActivity::class.java)
                    startActivityForResult(intent, Constants.Keys.RESULT_CODE_CONTACTS_SELECTION)
                }
                R.id.cancel or R.id.close-> {
                    alertDialog.dismiss()
                }
                R.id.proceed_dlg -> {
                    //make transfer request
                    context?.let { ovoP2pTransferConfirmViewModel.makeTransferConfirmCall(it, trnsfrReqDataMap) }
                }
            }
        }
    }

    fun createOvoP2pTransferReqMap(key: String, value: Any){
        if(!::trnsfrReqDataMap.isInitialized){
            trnsfrReqDataMap = HashMap()
        }
        trnsfrReqDataMap.put(key, value)
    }

    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return Constants.ScreenName.FORM_FRAGMENT
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.ovo_p2p_transfer_form, container,false)
        searchView = view.findViewById(R.id.search_no)
        searchView.onFocusChangeListener = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            searchView.setOnQueryTextListener(this)
        }
        saldoTextView = view.findViewById(R.id.saldo)
        trnsfrAmtEdtxtv = view.findViewById(R.id.trnsfr_amt_edtv)
        msgEdtxtv = view.findViewById(R.id.msg)
        proceedBtn = view.findViewById(R.id.proceed)
        proceedBtn.setOnClickListener(this)
        contactsImageView = view.findViewById(R.id.iv_contact)
        contactsImageView.setOnClickListener(this)
        searchNoHeader = view.findViewById(R.id.search_no_header)
        createAndSusbcribeToWalletBalVM()
        createAndSubscribeTransferRequestVM()
        createAndSubscribeTransferConfirmVM()
        context?.let { walletBalanceViewModel.fetchWalletDetails(it) }
        return view
    }

    fun createAndSusbcribeToWalletBalVM(){
        if(!::walletBalanceViewModel.isInitialized){
            if(activity != null) {
                walletBalanceViewModel = ViewModelProviders.of(this.activity!!).get(GetWalletBalanceViewModel::class.java)
                walletBalanceViewModel.walletLiveData?.observe(this.activity!!, Observer <WalletDataBase>{
                    if(it != null){
                        (activity as LoaderUiListener).hideProgressDialog()
                        saldoTextView.text = it.wallet?.balance ?: ""
                    }
                })
            }
        }
    }

    fun createAndSubscribeTransferRequestVM(){
        if(!::ovoP2pTransferRequestViewModel.isInitialized){
            if(activity != null){
                ovoP2pTransferRequestViewModel = ViewModelProviders.of(this.activity!!).get(OvoP2pTransferRequestViewModel::class.java)
                ovoP2pTransferRequestViewModel.ovoP2pTransferRequestBaseMutableLiveData?.observe(this.activity!!, Observer <OvoP2pTransferRequestBase>{
                    if(it != null){
                        (activity as LoaderUiListener).hideProgressDialog()
                        if(!TextUtils.isEmpty(it.ovoP2pTransferRequest.dstAccName)){
                            //show non ovo user confirmation dialog
                            showNonOvoUserConfirmDialog()
                        }
                        else{
                            //show ovo user confirmation dialog
                            showOvoUserConfirmationDialog()
                        }
                    }
                })
            }
        }
    }

    fun createAndSubscribeTransferConfirmVM(){
        if(!::ovoP2pTransferConfirmViewModel.isInitialized){
            if(activity != null){
                ovoP2pTransferConfirmViewModel = ViewModelProviders.of(this.activity!!).get(OvoP2pTrxnConfirmVM::class.java)
                ovoP2pTransferConfirmViewModel.txnConfirmMutableLiveData?.observe(this.activity!!, Observer <OvoP2pTransferConfirmBase>{
                    if(it != null){
                        (activity as LoaderUiListener).hideProgressDialog()
                        if(it.ovoP2pTransferConfirm!!.errors != null){
                            //show error page
                        }
                        else if(!it.ovoP2pTransferConfirm!!.rcvrLink){
                            //show non ovo success page
                        }
                        else if(it.ovoP2pTransferConfirm!!.rcvrLink){
                            if(!TextUtils.isEmpty(it.ovoP2pTransferConfirm!!.pinUrl)) {
                                //launch web view
                                if(context != null) {
                                    var intent: Intent = OvoP2pWebViewActivity.getWebViewIntent(context!!, it.ovoP2pTransferConfirm!!.pinUrl,
                                            Constants.Headers.TRANSFER_FORM_HEADER)
                                    activity?.startActivity(intent)
                                }
                            }
                            else{
                                it.ovoP2pTransferConfirm!!.transferId
                                // go to thankyou activity
                            }
                        }
                    }
                })
            }
        }
    }

    companion object{
        fun newInstance(): OvoFormFragment {
            return OvoFormFragment()
        }

        fun newInstance(bundle: Bundle): OvoFormFragment {
            val fragmentOVOP2PForm = newInstance()
            fragmentOVOP2PForm.setArguments(bundle)
            return fragmentOVOP2PForm
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        var id: Int = v?.id ?: -1
        if(id != -1){
            when(id){
                R.id.search_no -> {
                    if(hasFocus){
                        searchNoHeader.visibility = View.VISIBLE
                    }
                    else{
                        searchNoHeader.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val contacts = context?.let { newText?.let { it1 -> getPartialMatchContact(it, it1) } }
        val cursorAdapter = context?.let { newText?.let { it1 -> contacts?.let { it2 -> ContactsCursorAdapter(it, it2, it1) } } }
        searchView.suggestionsAdapter = cursorAdapter
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity != null){
            (activity as LoaderUiListener).setHeaderTitle(Constants.Headers.TRANSFER_FORM_HEADER)
        }
    }

    private fun getPartialMatchContact(context: Context, partial: String): Cursor? {
        val uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(partial))
        val selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?"
        val selectionArgs = arrayOf("%$partial%")
        return context.contentResolver.query(uri, null, selection, selectionArgs, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.activity?.let { walletBalanceViewModel.walletLiveData?.removeObservers(it) }
    }

    private fun showOvoUserConfirmationDialog() {
        alertDialog = OvoP2pUtil.getOvoUserTransferConfirmSubmitAlertDialog(activity, this, trnsfrReqDataMap).create()
        alertDialog.show()
    }

    private fun showNonOvoUserConfirmDialog(){
        alertDialog = OvoP2pUtil.getNonOvoUserConfirmationDailog(activity, this).create()
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constants.Keys.RESULT_CODE_CONTACTS_SELECTION) {
            val userName = data!!.getStringExtra(Constants.Keys.USER_NAME)
            val userNumber = data.getStringExtra(Constants.Keys.USER_NUMBER)
            val searchViewStr = "$userNumber-$userName"
            searchView.setQuery(searchViewStr, false)
        }
    }

}