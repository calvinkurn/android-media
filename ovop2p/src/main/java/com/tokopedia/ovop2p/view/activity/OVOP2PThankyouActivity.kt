package com.tokopedia.ovop2p.view.activity

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.DaggerOvoP2pTransferComponent
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.view.fragment.TxnSucsNonOvoUsr
import com.tokopedia.ovop2p.view.fragment.TxnSucsOvoUser
import com.tokopedia.ovop2p.view.interfaces.ActivityListener
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener

class OVOP2PThankyouActivity : BaseSimpleActivity(), LoaderUiListener, ActivityListener, HasComponent<OvoP2pTransferComponent> {

    private var transferId: String = ""
    private var nonOvoUser: Boolean = false
    private lateinit var loading: ProgressDialog
    private lateinit var ovoP2pTransferComponent: OvoP2pTransferComponent

    private fun initInjector() {
        ovoP2pTransferComponent = DaggerOvoP2pTransferComponent.builder().baseAppComponent(
                (applicationContext as BaseMainApplication).baseAppComponent).build()
    }

    override fun getComponent(): OvoP2pTransferComponent {
        if (!::ovoP2pTransferComponent.isInitialized) {
            initInjector()
        }
        return ovoP2pTransferComponent
    }

    override fun getNewFragment(): Fragment {
        var fragBundle = Bundle()
        fragBundle.putString(Constants.Keys.RECIEVER_NAME, PersistentCacheManager.instance.get(Constants.Keys.RECIEVER_NAME, String::class.java))
        fragBundle.putString(Constants.Keys.RECIEVER_PHONE, PersistentCacheManager.instance.get(Constants.Keys.RECIEVER_PHONE, String::class.java))
        if (nonOvoUser) {
            return TxnSucsNonOvoUsr.newInstance(fragBundle)
        } else {
            if (!TextUtils.isEmpty(transferId)) {
                fragBundle.putString(Constants.Keys.TRANSFER_ID, transferId)
                return TxnSucsOvoUser.newInstance(fragBundle)
            }
        }
        return Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setUserData()
        super.onCreate(savedInstanceState)
        updateTitle(Constants.Headers.TRANSFER_SUCCESS)
    }

    private fun setUserData() {
        var uri: Uri? = intent.data
        var listStr = uri?.let { UriUtil.destructureUri(ApplinkConst.OVOP2PTHANKYOUPAGE, it) }
        if (listStr?.size ?: 0 > 0) {
            transferId = listStr?.get(0) ?: ""
        }
        if(intent != null && intent.extras != null) {
            nonOvoUser = intent.extras.getBoolean(Constants.Keys.NON_OVO_SUCS)
            if(TextUtils.isEmpty(transferId) && intent.extras.containsKey(Constants.Keys.TRANSFER_ID)) {
                transferId = intent.extras.getString(Constants.Keys.TRANSFER_ID)
            }
        }
    }

    override fun addReplaceFragment(baseDaggerFragment: BaseDaggerFragment, replace: Boolean, tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (replace) {
            fragmentTransaction.replace(R.id.parent_view, baseDaggerFragment, tag)
        } else {
            fragmentTransaction.add(R.id.parent_view, baseDaggerFragment, tag)
        }
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun showProgressDialog() {
        if (!::loading.isInitialized) loading = ProgressDialog(this)
        with(loading) {
            setCancelable(false)
            setMessage(getString(R.string.title_loading))
            show()
        }
    }

    override fun hideProgressDialog() {
        if (loading != null)
            loading.dismiss()
    }

    override fun setHeaderTitle(title: String) {
        updateTitle(title)
    }
}