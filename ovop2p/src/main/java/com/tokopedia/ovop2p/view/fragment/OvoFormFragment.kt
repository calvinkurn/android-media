package com.tokopedia.ovop2p

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.model.WalletDataBase
import com.tokopedia.ovop2p.view.activity.AllContactsActivity
import com.tokopedia.ovop2p.view.adapters.ContactsCursorAdapter
import com.tokopedia.ovop2p.viewmodel.GetWalletBalanceViewModel
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

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if(id != -1){
            when(id){
                R.id.proceed -> {
                    //make request call
                }
                R.id.iv_contact -> {
                    val intent = Intent(activity, AllContactsActivity::class.java)
                    startActivityForResult(intent, Constants.Keys.RESULT_CODE_CONTACTS_SELECTION)
                }
            }
        }
    }

    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.ovo_p2p_transfer_form, container,false)
        searchView = view.findViewById(R.id.search_no)
        searchView.onFocusChangeListener = this
        searchView.setOnQueryTextListener(this)
        saldoTextView = view.findViewById(R.id.saldo)
        trnsfrAmtEdtxtv = view.findViewById(R.id.trnsfr_amt_edtv)
        msgEdtxtv = view.findViewById(R.id.msg)
        proceedBtn = view.findViewById(R.id.proceed)
        proceedBtn.setOnClickListener(this)
        contactsImageView = view.findViewById(R.id.iv_contact)
        contactsImageView.setOnClickListener(this)
        searchNoHeader = view.findViewById(R.id.search_no_header)
        return view
    }

    fun createAndSusbcribeToWalletBalVM(){
        if(!::walletBalanceViewModel.isInitialized){
            if(this.activity != null) {
                walletBalanceViewModel = ViewModelProviders.of(this.activity!!).get(GetWalletBalanceViewModel::class.java)
                walletBalanceViewModel.walletLiveData?.observe(this.activity!!, Observer <WalletDataBase>{
                    if(it != null){
                        saldoTextView.text = it.wallet?.balance ?: ""
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

}