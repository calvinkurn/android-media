package com.tokopedia.ovop2p.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.View

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.view.fragment.OvoP2PForm
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.DaggerOvoP2pTransferComponent
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.view.interfaces.ActivityListener
import com.tokopedia.ovop2p.view.interfaces.LoaderUiListener
import java.util.ArrayList

class OvoP2PFormActivity : BaseSimpleActivity(), HasComponent<OvoP2pTransferComponent>, LoaderUiListener, ActivityListener {

    private lateinit var ovoP2pTransferComponent: OvoP2pTransferComponent
    private lateinit var loading: View
    private lateinit var parentView: View
    private lateinit var userPhoneNo: String


    override fun getNewFragment(): Fragment {
        var bundle = Bundle()
        if(::userPhoneNo.isInitialized) {
            bundle.putString(Constants.Keys.USER_NUMBER, userPhoneNo)
        }
        return OvoP2PForm.newInstance(bundle)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_ovop2p_transfer
    }

    override fun getComponent(): OvoP2pTransferComponent {
        if (!::ovoP2pTransferComponent.isInitialized) {
            initInjector()
        }
        return ovoP2pTransferComponent
    }

    private fun initInjector() {
        ovoP2pTransferComponent = DaggerOvoP2pTransferComponent.builder().baseAppComponent(
                (applicationContext as BaseMainApplication).baseAppComponent).build()

    }

    override fun showProgressDialog() {
        if(!::parentView.isInitialized) parentView = findViewById(R.id.parent_view)
        parentView.visibility = View.GONE
        if (!::loading.isInitialized) loading = findViewById(R.id.progressbar_cntnr)
        loading.visibility = View.VISIBLE
    }

    override fun hideProgressDialog() {
        if (::loading.isInitialized)
            loading.visibility = View.GONE
        if(::parentView.isInitialized)
            parentView.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setUserData()
        super.onCreate(savedInstanceState)
    }

    override fun setHeaderTitle(title: String) {
        updateTitle(title)
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

    private fun setUserData() {
        var uri: Uri? = intent.data
        var paramMap = uri?.let { UriUtil.destructureUriToMap(ApplinkConst.OVOP2PTRANSFERFORM, it, true) }
        if (paramMap?.size ?: 0 > 0) {
            if (paramMap != null) {
                if(paramMap.containsKey(Constants.Keys.PHONE)) {
                    userPhoneNo = paramMap[Constants.Keys.PHONE].toString()
                }
            }
        }
    }
}
