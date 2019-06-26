package com.tokopedia.ovop2p.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.DaggerOvoP2pTransferComponent
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.view.fragment.FragmentTransactionSuccessNonOvoUser
import com.tokopedia.ovop2p.view.fragment.FragmentTransactionSuccessOvoUser
import com.tokopedia.ovop2p.view.interfaces.ActivityListener
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener

class OVOP2PThankyouActivity : BaseSimpleActivity(),LoaderUiListener, ActivityListener, HasComponent<OvoP2pTransferComponent> {

    private var transferId: String = ""
    private var nonOvoUser: Boolean = false
    private lateinit var loading: ProgressDialog
    private lateinit var ovoP2pTransferComponent: OvoP2pTransferComponent

    object DeeplinkIntents {
        @DeepLink(Constants.AppLinks.OVOP2PTHANKYOUPAGE)
        @JvmStatic
        fun getCallingStartUpgradeToOvo(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, OVOP2PThankyouActivity::class.java)
                    .setData(uri.build())
                    .putExtras(extras)
        }
    }

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
        if(intent != null){
            nonOvoUser = intent.extras.getBoolean(Constants.Keys.NON_OVO_SUCS)
            transferId = intent.extras.getString(Constants.PlaceHolders.TRNSFER_ID_PLCHLDR)
            if(nonOvoUser){
                return FragmentTransactionSuccessNonOvoUser.newInstance()
            }
            else{
                if(!TextUtils.isEmpty(transferId)){
                    var fragBundle: Bundle = Bundle()
                    fragBundle.putString(Constants.Keys.TRANSFER_ID, transferId)
                    return FragmentTransactionSuccessOvoUser.newInstance(fragBundle)
                }
            }
        }
        return Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(Constants.Headers.TRANSFER_SUCCESS)
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