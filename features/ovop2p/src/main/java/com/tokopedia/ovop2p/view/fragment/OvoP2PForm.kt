package com.tokopedia.ovop2p.view.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.util.AnalyticsUtil
import com.tokopedia.ovop2p.util.OvoP2pUtil
import com.tokopedia.ovop2p.view.activity.AllContactsActivity
import com.tokopedia.ovop2p.view.activity.OVOP2PThankyouActivity
import com.tokopedia.ovop2p.view.activity.OvoP2pWebViewActivity
import com.tokopedia.ovop2p.view.adapters.ContactsCursorAdapter
import com.tokopedia.ovop2p.view.interfaces.ActivityListener
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener
import com.tokopedia.ovop2p.view.viewStates.GoToThankYouPage
import com.tokopedia.ovop2p.view.viewStates.OpenPinChlngWebView
import com.tokopedia.ovop2p.view.viewStates.TransferConfErrorPage
import com.tokopedia.ovop2p.view.viewStates.TransferConfErrorSnkBar
import com.tokopedia.ovop2p.view.viewStates.TransferConfirmState
import com.tokopedia.ovop2p.view.viewStates.TransferReqData
import com.tokopedia.ovop2p.view.viewStates.TransferReqErrorPage
import com.tokopedia.ovop2p.view.viewStates.TransferReqErrorSnkBar
import com.tokopedia.ovop2p.view.viewStates.TransferReqNonOvo
import com.tokopedia.ovop2p.view.viewStates.TransferRequestState
import com.tokopedia.ovop2p.view.viewStates.WalletBalanceState
import com.tokopedia.ovop2p.view.viewStates.WalletData
import com.tokopedia.ovop2p.view.viewStates.WalletError
import com.tokopedia.ovop2p.viewmodel.OvoDetailViewModel
import javax.inject.Inject

class OvoP2PForm : BaseDaggerFragment(), View.OnClickListener, SearchView.OnQueryTextListener {

    private lateinit var searchView: SearchView
    private lateinit var saldoTextView: TextView
    private lateinit var trnsfrAmtEdtxtv: EditText
    private lateinit var msgEdtxtv: EditText
    private lateinit var proceedBtn: Button
    private lateinit var contactsImageView: ImageView
    private lateinit var searchNoHeader: TextView
    private lateinit var alertDialog: AlertDialog
    private lateinit var trnsfrReqDataMap: HashMap<String, Any>
    private lateinit var amtErrorTxtv: TextView
    private var rcvrPhnNo: String = ""
    private var rcvrAmt: Long = 0
    private var sndrAmt: Long = 0
    private var rcvrMsg: String = ""
    private var rcvrName: String = ""
    private var nonOvoUsr: Boolean = false
    private lateinit var errorSnackbar: Snackbar
    private lateinit var permissionsToRequest: MutableList<String>
    private var isPermissionGotDenied: Boolean = false
    private val REQUEST_CONTACTS__CAMERA_PERMISSION = 123

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val walletDetailViewModel: OvoDetailViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider =
            ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(OvoDetailViewModel::class.java)
    }


    override fun onClick(v: View?) {
        val id: Int = v?.id ?: -1
        if (id != -1) {
            when (id) {
                R.id.proceed -> {
                    //make request call
                    rcvrMsg = msgEdtxtv.text.toString()
                    addUserDataToMap()
                    (activity as LoaderUiListener).showProgressDialog()
                    walletDetailViewModel.makeTransferRequestCall(trnsfrReqDataMap)
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_TRNSFR, "", AnalyticsUtil.EventAction.CLK_LNJKTN)
                    }
                }
                R.id.iv_contact -> {
                    val intent = Intent(activity, AllContactsActivity::class.java)
                    startActivityForResult(intent, Constants.Keys.RESULT_CODE_CONTACTS_SELECTION)
                }
                R.id.cancel -> {
                    if (activity?.isFinishing == false) alertDialog.dismiss()
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_CONF_TRANSFER, "", AnalyticsUtil.EventAction.CLK_BTLKN)
                    }
                }
                R.id.close -> {
                    if (activity?.isFinishing == false) alertDialog.dismiss()
                    nonOvoUsr = false
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_CONF_TRANSFER, "", AnalyticsUtil.EventAction.CLK_BTLKN)
                    }
                }
                R.id.proceed_dlg -> {
                    //make transfer confirm request
                    if (activity?.isFinishing == false) alertDialog.dismiss()
                    (activity as LoaderUiListener).showProgressDialog()
                    walletDetailViewModel.makeTransferConfirmCall(trnsfrReqDataMap)
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_CONF_TRANSFER, "", AnalyticsUtil.EventAction.CLK_TRNSFR)
                    }
                }
                R.id.proceed_dlg_non_ovo -> {
                    if (activity?.isFinishing == false) alertDialog.dismiss()
                    (activity as LoaderUiListener).showProgressDialog()
                    walletDetailViewModel.makeTransferConfirmCall(trnsfrReqDataMap)
                    context?.let {
                        AnalyticsUtil.sendEvent(it, AnalyticsUtil.EventName.CLICK_OVO,
                                AnalyticsUtil.EventCategory.OVO_CONF_TRANSFER, "", AnalyticsUtil.EventAction.CLK_TRNSFR)
                    }
                }
                com.tokopedia.design.R.id.btn_ok -> {
                    errorSnackbar.let {
                        if (it.isShownOrQueued) it.dismiss()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGotDenied) {
            activity?.finish()
            return
        } else {
            askForPermissions()
        }
    }

    private fun askForPermissions() {
        val permissions: Array<String> = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS)
        permissionsToRequest = ArrayList()
        for (permission in permissions) {
            if (activity?.let { ActivityCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                (permissionsToRequest as ArrayList<String>).add(permission)
            }
        }
        if ((permissionsToRequest as ArrayList<String>).isNotEmpty()) {
            requestPermissions((permissionsToRequest as ArrayList<String>).toTypedArray(), REQUEST_CONTACTS__CAMERA_PERMISSION)
        } else {
            setSearchQueryListener()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size == permissionsToRequest.size) {
            var grantCount = 0
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isPermissionGotDenied = true
                    break
                }
                grantCount++
            }
            if (grantCount == grantResults.size) {
                isPermissionGotDenied = false
            }
        }
    }

    private fun createOvoP2pTransferReqMap(key: String, value: Any) {
        if (!::trnsfrReqDataMap.isInitialized) {
            trnsfrReqDataMap = HashMap()
        }
        trnsfrReqDataMap.put(key, value)
    }

    private fun addUserDataToMap() {
        createOvoP2pTransferReqMap(Constants.Keys.AMOUNT, rcvrAmt)
        createOvoP2pTransferReqMap(Constants.Keys.NAME, rcvrName)
        createOvoP2pTransferReqMap(Constants.Keys.FORMATTED_AMOUNT,
                CurrencyFormatUtil.getThousandSeparatorString(rcvrAmt.toDouble(), false, 0).formattedString)
        createOvoP2pTransferReqMap(Constants.Keys.TO_PHN_NO, rcvrPhnNo)
        createOvoP2pTransferReqMap(Constants.Keys.MESSAGE, rcvrMsg)
    }

    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return Constants.ScreenName.FORM_FRAGMENT
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.ovo_p2p_transfer_form, container, false)
        initViews(view)
        createAndSubscribeToWalletBalVM()
        createAndSubscribeTransferRequestVM()
        createAndSubscribeTransferConfirmVM()
        (activity as LoaderUiListener).showProgressDialog()
        setTextSenderAmountWatcher()
        return view
    }

    private fun initViews(view: View){
        searchView = view.findViewById(R.id.search_no)
        saldoTextView = view.findViewById(R.id.saldo)
        trnsfrAmtEdtxtv = view.findViewById(R.id.trnsfr_amt_edtv)
        msgEdtxtv = view.findViewById(R.id.msg_edtxt)
        proceedBtn = view.findViewById(R.id.proceed)
        proceedBtn.setOnClickListener(this)
        contactsImageView = view.findViewById(R.id.iv_contact)
        contactsImageView.setOnClickListener(this)
        searchNoHeader = view.findViewById(R.id.search_no_header)
        amtErrorTxtv = view.findViewById(R.id.amt_err_txtv)
    }

    private fun setUserData() {
        if (arguments?.containsKey(Constants.Keys.USER_NUMBER)!!) {
            var phoneNo = requireArguments().getString(Constants.Keys.USER_NUMBER)
            if (!TextUtils.isEmpty(phoneNo)) {
                searchView.setQuery(phoneNo, false)
            }
        }
    }

    private fun createAndSubscribeToWalletBalVM() {
        walletDetailViewModel.fetchWalletDetails()
        walletDetailViewModel.walletLiveData.observe(
            viewLifecycleOwner,
            getWalletVMObserver(activity as LoaderUiListener)
        )
    }

    private fun getWalletVMObserver(loaderUiListener: LoaderUiListener): Observer<WalletBalanceState> {
        return Observer {
            loaderUiListener.hideProgressDialog()
            when (it) {
                is WalletError -> {
                    showErrorSnackBar(it.errMsg)
                }
                is WalletData -> {
                    saldoTextView.text = it.cashBalance
                    sndrAmt = it.rawCashBalance
                }
            }
        }
    }

    private fun createAndSubscribeTransferRequestVM() {
        walletDetailViewModel.transferReqBaseMutableLiveData.observe(
                    viewLifecycleOwner,
                    getTransferReqObserver(activity as LoaderUiListener)
                )
            }


    private fun getTransferReqObserver(loaderUiListener: LoaderUiListener): Observer<TransferRequestState> {
        return Observer {
            loaderUiListener.hideProgressDialog()
            when (it) {
                is TransferReqErrorSnkBar -> {
                    showErrorSnackBar(it.errMsg)
                }
                is TransferReqErrorPage -> {
                    gotoErrorPage(it.errMsg)
                }
                is TransferReqNonOvo -> {
                    showNonOvoUserConfirmDialog()
                }
                is TransferReqData -> {
                    if (!TextUtils.isEmpty(it.dstAccName)) {
                        rcvrName = it.dstAccName
                        createOvoP2pTransferReqMap(Constants.Keys.NAME, rcvrName)
                    }
                    showOvoUserConfirmationDialog()
                }
            }
        }
    }

    private fun createAndSubscribeTransferConfirmVM() {
        walletDetailViewModel.txnConfirmMutableLiveData.observe(
            viewLifecycleOwner,
            getTransferConfObserver(activity as LoaderUiListener)
        )
            }


    private fun getTransferConfObserver(loaderUiListener: LoaderUiListener): Observer<TransferConfirmState> {
        return Observer {
            loaderUiListener.hideProgressDialog()
            when (it) {
                is TransferConfErrorPage -> {
                    gotoErrorPage(it.errMsg)
                }
                is TransferConfErrorSnkBar -> {
                    showErrorSnackBar(it.errMsg)
                }
                is GoToThankYouPage -> {
                    saveRcvrData()
                    gotoThankYouActivity(it.transferId, nonOvoUsr)
                }
                is OpenPinChlngWebView -> {
                    saveRcvrData()
                    if (context != null) {
                        var intent: Intent = OvoP2pWebViewActivity.getWebViewIntent(
                            requireContext(), it.pinUrl,
                            Constants.Headers.TRANSFER_FORM_HEADER
                        )
                        activity?.startActivity(intent)
                    }
                }
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
                changeProceedBtnState(s.toString(), searchView.query.toString())
                var enteredAmt: Long = 0
                if (!TextUtils.isEmpty(s.toString()))
                    enteredAmt = OvoP2pUtil.extractNumbersFromString(s.toString()).toLong()
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

    private fun checkRcvrAmtValidity(enteredAmtStr: String): Boolean {
        var rcvrAmtValid = false
        if (!TextUtils.isEmpty(enteredAmtStr)) {
            var enteredAmt = OvoP2pUtil.extractNumbersFromString(enteredAmtStr).toLong()
            if (enteredAmt < Constants.Thresholds.MIN_TRANSFER_LIMIT) {
                amtErrorTxtv.text = resources.getString(R.string.minimal_trnsfr)
                amtErrorTxtv.visibility = View.VISIBLE
            } else if (enteredAmt > sndrAmt) {
                amtErrorTxtv.text = resources.getString(R.string.amt_more_thn_bal)
                amtErrorTxtv.visibility = View.VISIBLE
            } else {
                amtErrorTxtv.visibility = View.GONE
                rcvrAmt = enteredAmt
                rcvrAmtValid = true
            }
        } else {
            amtErrorTxtv.visibility = View.GONE
        }
        return rcvrAmtValid
    }

    private fun checkRcvrPhnNoValidity(rcvrPhnNoStr: String): Boolean {
        var rcvrPhnNoValid = false
        if (!TextUtils.isEmpty(rcvrPhnNoStr)) {
            rcvrPhnNo = OvoP2pUtil.extractNumbersFromString(rcvrPhnNoStr)
            if (!TextUtils.isEmpty(rcvrPhnNo)) {
                rcvrPhnNoValid = true
            }
        }
        return rcvrPhnNoValid
    }

    private fun changeProceedBtnState(enteredAmtStr: String, rcvrPhnNoStr: String) {
        var isAmtValid = checkRcvrAmtValidity(enteredAmtStr)
        var isPhnNoValid = checkRcvrPhnNoValidity(rcvrPhnNoStr)
        var enable = isAmtValid && isPhnNoValid
        proceedBtn.isEnabled = enable
    }

    companion object {
        fun newInstance(): OvoP2PForm {
            return OvoP2PForm()
        }
        const val GENERAL_ERROR = "Ada yang salah. Silakan coba lagi"

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
        changeProceedBtnState(trnsfrAmtEdtxtv.text.toString(), searchView.query.toString())
        val contacts = context?.let {
            newText?.let { it1 -> getPartialMatchContact(it, it1) }
        }
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
        setUserData()
    }

    private fun getPartialMatchContact(context: Context, partial: String): Cursor? {
        val uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(partial))
        val selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?"
        val selectionArgs = arrayOf("%$partial%")
        var cur = context.contentResolver.query(uri, null, selection, selectionArgs, null)
        return cur
    }

    private fun showOvoUserConfirmationDialog() {
        createOvoP2pTransferReqMap(Constants.Keys.RECIEVER_NAME, rcvrName)
        alertDialog = OvoP2pUtil.getOvoUserTransferConfirmSubmitAlertDialog(activity, this, trnsfrReqDataMap).create()
        if (activity?.isFinishing == false) alertDialog.show()
        nonOvoUsr = false
    }

    fun setContactsData(rcvrName: String, rcvrPhone: String) {
        this.rcvrName = rcvrName
        this.rcvrPhnNo = OvoP2pUtil.extractNumbersFromString(rcvrPhone)
        setSearchViewQuery()
    }

    private fun showNonOvoUserConfirmDialog() {
        alertDialog = OvoP2pUtil.getNonOvoUserConfirmationDailog(activity, this).create()
        if (activity?.isFinishing == false) {
            alertDialog.show()
        }
        nonOvoUsr = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constants.Keys.RESULT_CODE_CONTACTS_SELECTION) {
            this.rcvrName = data!!.getStringExtra(Constants.Keys.USER_NAME) ?: ""
            var phoneNo = OvoP2pUtil.extractNumbersFromString(data.getStringExtra(Constants.Keys.USER_NUMBER)?: "")
            if(phoneNo.length <= Constants.Thresholds.PHONE_NO_LENGTH) {
                this.rcvrPhnNo = phoneNo
            }
            setSearchQueryListener()
            setSearchViewQuery()
        }
    }

    private fun setSearchQueryListener() {
        if (::searchView.isInitialized) {
            searchView.setOnQueryTextListener(this)
        }
    }

    private fun setSearchViewQuery() {
        var searchViewStr = ""
        searchViewStr = if(!TextUtils.isEmpty(rcvrName)) {
            "$rcvrPhnNo - $rcvrName"
        }
        else{
            rcvrPhnNo
        }
        searchView.setQuery(searchViewStr, false)
    }

    private fun showErrorSnackBar(errMsg: String) {
        activity?.let {
            errorSnackbar = OvoP2pUtil.createErrorSnackBar(it, this, errMsg)
            errorSnackbar.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchView.suggestionsAdapter?.let {
            (it as ContactsCursorAdapter).cursor?.apply{
                close()
            }
        }
    }
}