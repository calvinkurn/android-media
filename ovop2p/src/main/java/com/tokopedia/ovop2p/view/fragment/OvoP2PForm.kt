package com.tokopedia.ovop2p

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.model.WalletDataBase
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.activity.AllContactsActivity
import com.tokopedia.ovop2p.view.activity.OVOP2PThankyouActivity
import com.tokopedia.ovop2p.view.activity.OvoP2pWebViewActivity
import com.tokopedia.ovop2p.view.adapters.ContactsCursorAdapter
import com.tokopedia.ovop2p.view.fragment.TransferError
import com.tokopedia.ovop2p.view.interfaces.ActivityListener
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener
import com.tokopedia.ovop2p.viewmodel.GetWalletBalanceViewModel
import com.tokopedia.ovop2p.viewmodel.OvoP2pTransferRequestViewModel
import com.tokopedia.ovop2p.viewmodel.OvoP2pTrxnConfirmVM

class OvoP2PForm : BaseDaggerFragment(), View.OnClickListener, SearchView.OnQueryTextListener {

    private lateinit var searchView: SearchView
    private lateinit var saldoTextView: TextView
    private lateinit var trnsfrAmtEdtxtv: EditText
    private lateinit var msgEdtxtv: EditText
    private lateinit var proceedBtn: Button
    private lateinit var contactsImageView: ImageView
    private lateinit var searchNoHeader: TextView
    private lateinit var walletBalanceViewModel: GetWalletBalanceViewModel
    private lateinit var alertDialog: AlertDialog
    private lateinit var ovoP2pTransferRequestViewModel: OvoP2pTransferRequestViewModel
    private lateinit var ovoP2pTransferConfirmViewModel: OvoP2pTrxnConfirmVM
    private lateinit var trnsfrReqDataMap: HashMap<String, Any>
    private lateinit var amtErrorTxtv: TextView
    private var rcvrPhnNo: String = "9582820386"
    private var rcvrAmt: Long = 0
    private var sndrAmt: Long = 0
    private var rcvrMsg: String = ""
    private var rcvrName: String = "Varun"
    private var nonOvoUsr: Boolean = false
    private lateinit var errorSnackbar: Snackbar


    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if (id != -1) {
            when (id) {
                R.id.proceed -> {
                    //make request call
                    rcvrMsg = msgEdtxtv.text.toString()
                    createOvoP2pTransferReqMap(Constants.Keys.AMOUNT, rcvrAmt)
                    createOvoP2pTransferReqMap(Constants.Keys.NAME, rcvrName)
                    createOvoP2pTransferReqMap(Constants.Keys.FORMATTED_AMOUNT, rcvrAmt)
                    createOvoP2pTransferReqMap(Constants.Keys.TO_PHN_NO, rcvrPhnNo)
                    createOvoP2pTransferReqMap(Constants.Keys.MESSAGE, rcvrMsg)
                    (activity as LoaderUiListener).showProgressDialog()
                    context?.let { ovoP2pTransferRequestViewModel.makeTransferRequestCall(it, trnsfrReqDataMap) }
                }
                R.id.iv_contact -> {
                    val intent = Intent(activity, AllContactsActivity::class.java)
                    startActivityForResult(intent, Constants.Keys.RESULT_CODE_CONTACTS_SELECTION)
                }
                R.id.cancel -> {
                    alertDialog.dismiss()
                }
                R.id.close -> {
                    alertDialog.dismiss()
                    nonOvoUsr = false
                }
                R.id.proceed_dlg -> {
                    //make transfer confirm request
                    alertDialog.dismiss()
                    (activity as LoaderUiListener).showProgressDialog()
                    context?.let { ovoP2pTransferConfirmViewModel.makeTransferConfirmCall(it, trnsfrReqDataMap) }
                }
                R.id.btn_ok -> {
                    errorSnackbar.let {
                        if (it.isShownOrQueued) it.dismiss()
                    }
                }
            }
        }
    }

    fun createOvoP2pTransferReqMap(key: String, value: Any) {
        if (!::trnsfrReqDataMap.isInitialized) {
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
        var view: View = inflater.inflate(R.layout.ovo_p2p_transfer_form, container, false)
        searchView = view.findViewById(R.id.search_no)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            searchView.setOnQueryTextListener(this)
        }
        saldoTextView = view.findViewById(R.id.saldo)
        trnsfrAmtEdtxtv = view.findViewById(R.id.trnsfr_amt_edtv)
        msgEdtxtv = view.findViewById(R.id.msg_edtxt)
        proceedBtn = view.findViewById(R.id.proceed)
        proceedBtn.setOnClickListener(this)
        contactsImageView = view.findViewById(R.id.iv_contact)
        contactsImageView.setOnClickListener(this)
        searchNoHeader = view.findViewById(R.id.search_no_header)
        amtErrorTxtv = view.findViewById(R.id.amt_err_txtv)
        createAndSusbcribeToWalletBalVM()
        createAndSubscribeTransferRequestVM()
        createAndSubscribeTransferConfirmVM()
        context?.let { walletBalanceViewModel.fetchWalletDetails(it) }
        (activity as LoaderUiListener).showProgressDialog()
        setTextSenderAmountWatcher()
        return view
    }

    private fun createAndSusbcribeToWalletBalVM() {
        if (!::walletBalanceViewModel.isInitialized) {
            if (activity != null) {
                walletBalanceViewModel = ViewModelProviders.of(this.activity!!).get(GetWalletBalanceViewModel::class.java)
                walletBalanceViewModel.walletLiveData?.observe(this, Observer<WalletDataBase> {
                    if (it != null) {
                        (activity as LoaderUiListener).hideProgressDialog()
                        saldoTextView.text = it.wallet?.cashBalance ?: ""
                        if (!TextUtils.isEmpty(saldoTextView.text)) {
                            sndrAmt = it.wallet?.rawCashBalance?.toLong() ?: 0
                        }
                    } else {
                        showErrorSnackBar()
                    }
                })
            }
        }
    }

    private fun createAndSubscribeTransferRequestVM() {
        if (!::ovoP2pTransferRequestViewModel.isInitialized) {
            if (activity != null) {
                ovoP2pTransferRequestViewModel = ViewModelProviders.of(this.activity!!).get(OvoP2pTransferRequestViewModel::class.java)
                ovoP2pTransferRequestViewModel.ovoP2pTransferRequestBaseMutableLiveData?.observe(this, Observer<OvoP2pTransferRequestBase> {
                    if (it != null) {
                        (activity as LoaderUiListener).hideProgressDialog()
                        if (it.ovoP2pTransferRequest.errors != null && (it.ovoP2pTransferRequest.errors!!.isNotEmpty())) {
                            it.ovoP2pTransferRequest.errors?.let { it1 ->
                                it1[0][Constants.Keys.MESSAGE]?.let { it2 ->
                                    if (it2.contentEquals(Constants.Messages.NON_OVO_USER)) {
                                        //show non ovo user confirmation dialog
                                        showNonOvoUserConfirmDialog()
                                    } else {
                                        gotoErrorPage(it2)
                                    }
                                }
                            }
                        } else {
                            //show ovo user confirmation dialog
                            var dstAccName = it.ovoP2pTransferRequest.dstAccName
                            if (!TextUtils.isEmpty(dstAccName)) {
                                rcvrName = dstAccName
                                createOvoP2pTransferReqMap(Constants.Keys.NAME, rcvrName)
                            }
                            showOvoUserConfirmationDialog()
                        }
                    } else {
                        showErrorSnackBar()
                    }
                })
            }
        }
    }

    private fun createAndSubscribeTransferConfirmVM() {
        if (!::ovoP2pTransferConfirmViewModel.isInitialized) {
            if (activity != null) {
                ovoP2pTransferConfirmViewModel = ViewModelProviders.of(this.activity!!).get(OvoP2pTrxnConfirmVM::class.java)
                ovoP2pTransferConfirmViewModel.txnConfirmMutableLiveData?.observe(this, Observer<OvoP2pTransferConfirmBase> {
                    if (it != null) {
                        (activity as LoaderUiListener).hideProgressDialog()
                        if (it.ovoP2pTransferConfirm!!.errors != null &&
                                it.ovoP2pTransferConfirm!!.errors?.size ?: 0 > 0) {
                            //show error page
                            it.ovoP2pTransferConfirm!!.errors?.let { it1 ->
                                it1[0][Constants.Keys.MESSAGE]?.let { it2 ->
                                    gotoErrorPage(it2)
                                }
                            }
                        } else {
                            saveRcvrData()
                            if (nonOvoUsr) {
                                //show non ovo success page
                                gotoThankYouActivity(it.ovoP2pTransferConfirm!!.transferId, true)
                            } else if (!TextUtils.isEmpty(it.ovoP2pTransferConfirm!!.pinUrl)) {
                                //launch web view
                                if (context != null) {
                                    var intent: Intent = OvoP2pWebViewActivity.getWebViewIntent(context!!, it.ovoP2pTransferConfirm!!.pinUrl,
                                            Constants.Headers.TRANSFER_FORM_HEADER)
                                    activity?.startActivity(intent)
                                }
                            } else {
                                it.ovoP2pTransferConfirm!!.transferId
                                // go to thankyou activity
                                gotoThankYouActivity(it.ovoP2pTransferConfirm!!.transferId, false)
                            }
                        }
                    } else {
                        showErrorSnackBar()
                    }
                })
            }
        }
    }

    private fun saveRcvrData() {
        PersistentCacheManager.instance.put(Constants.Keys.RECIEVER_PHONE, rcvrPhnNo)
        PersistentCacheManager.instance.put(Constants.Keys.RECIEVER_NAME, rcvrName)
    }


    private fun gotoErrorPage(errMsg: String) {
        var fragment: BaseDaggerFragment = TransferError.createInstance()
        var bundle = Bundle()
        bundle.putString(Constants.Keys.ERR_MSG_ARG, errMsg)
        fragment.arguments = bundle
        (activity as ActivityListener).addReplaceFragment(fragment, true, TransferError.TAG)
    }

    private fun gotoThankYouActivity(transferId: String, nonOvo: Boolean) {
        var intent = Intent(activity, OVOP2PThankyouActivity::class.java)
        intent.putExtra(Constants.Keys.TRANSFER_ID, transferId)
        intent.putExtra(Constants.Keys.NON_OVO_SUCS, nonOvo)
        activity?.startActivity(intent)
        activity?.finish()
    }

    private fun setTextSenderAmountWatcher() {
        trnsfrAmtEdtxtv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                trnsfrAmtEdtxtv.removeTextChangedListener(this)
                var enteredAmt: Long = 0
                if (!TextUtils.isEmpty(s.toString())) {
                    enteredAmt = OvoP2pUtil.extractNumbersFromString(s.toString()).toLong()
                    if (enteredAmt < Constants.Thresholds.MIN_TRANSFER_LIMIT) {
                        amtErrorTxtv.text = Constants.Messages.MINIMAL_TRNSFR
                        amtErrorTxtv.visibility = View.VISIBLE
                        proceedBtn.isEnabled = false
                    } else if (enteredAmt > sndrAmt) {
                        amtErrorTxtv.text = Constants.Messages.AMT_MORE_THN_BAL
                        amtErrorTxtv.visibility = View.VISIBLE
                        proceedBtn.isEnabled = false
                    } else {
                        rcvrAmt = enteredAmt
                        if (!TextUtils.isEmpty(searchView.query)) {
                            rcvrPhnNo = OvoP2pUtil.checkValidRcvrPhoneEntry(searchView.query.toString(), rcvrPhnNo)
                            rcvrPhnNo = OvoP2pUtil.extractNumbersFromString(rcvrPhnNo)
                            proceedBtn.isEnabled = true
                            amtErrorTxtv.visibility = View.GONE
                        }
                    }
                } else {
                    amtErrorTxtv.visibility = View.GONE
                }
                val rpFormattedString = CurrencyFormatUtil.getThousandSeparatorString(enteredAmt.toDouble(), false, 0)
                trnsfrAmtEdtxtv.setText(rpFormattedString.formattedString)
                trnsfrAmtEdtxtv.setSelection(rpFormattedString.selection)
                trnsfrAmtEdtxtv.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    companion object {
        fun newInstance(): OvoP2PForm {
            return OvoP2PForm()
        }

        fun newInstance(bundle: Bundle): OvoP2PForm {
            val fragmentOVOP2PForm = newInstance()
            fragmentOVOP2PForm.arguments = bundle
            return fragmentOVOP2PForm
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        setSearchViewHeader(!TextUtils.isEmpty(newText))
        val contacts = context?.let { newText?.let { it1 -> getPartialMatchContact(it, it1) } }
        val cursorAdapter = context?.let {
            newText?.let { it1 ->
                contacts?.let { it2 ->
                    ContactsCursorAdapter(it, it2, it1,
                            ::setContactsData)
                }
            }
        }
        searchView.suggestionsAdapter = cursorAdapter
        return true
    }

    private fun setSearchViewHeader(showheader: Boolean) {
        if (showheader) {
            searchNoHeader.visibility = View.VISIBLE
        } else {
            searchNoHeader.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            (activity as LoaderUiListener).setHeaderTitle(Constants.Headers.TRANSFER_FORM_HEADER)
        }
        saveRcvrData()
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
        createOvoP2pTransferReqMap(Constants.Keys.RECIEVER_NAME, rcvrName)
        alertDialog = OvoP2pUtil.getOvoUserTransferConfirmSubmitAlertDialog(activity, this, trnsfrReqDataMap).create()
        alertDialog.show()
        nonOvoUsr = false
    }

    fun setContactsData(rcvrName: String, rcvrPhone: String) {
        this.rcvrName = rcvrName
        this.rcvrPhnNo = OvoP2pUtil.extractNumbersFromString(rcvrPhone)
        setSearchViewQuery()
    }

    private fun showNonOvoUserConfirmDialog() {
        alertDialog = OvoP2pUtil.getNonOvoUserConfirmationDailog(activity, this).create()
        alertDialog.show()
        nonOvoUsr = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constants.Keys.RESULT_CODE_CONTACTS_SELECTION) {
            this.rcvrName = data!!.getStringExtra(Constants.Keys.USER_NAME)
            this.rcvrPhnNo = OvoP2pUtil.extractNumbersFromString(data.getStringExtra(Constants.Keys.USER_NUMBER))
            setSearchViewQuery()
        }
    }

    private fun setSearchViewQuery() {
        val searchViewStr = "$rcvrPhnNo - $rcvrName"
        searchView.setQuery(searchViewStr, false)
    }

    private fun showErrorSnackBar() {
        activity?.let {
            errorSnackbar = OvoP2pUtil.createErrorSnackBar(it, this, Constants.Messages.GENERAL_ERROR)
            errorSnackbar.show()
        }
    }

}