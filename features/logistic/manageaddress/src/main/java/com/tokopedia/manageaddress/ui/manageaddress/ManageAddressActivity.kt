package com.tokopedia.manageaddress.ui.manageaddress

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.ActivityManageAddressBinding
import com.tokopedia.manageaddress.di.DaggerManageAddressComponent
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.ui.manageaddress.mainaddress.MainAddressFragment

class ManageAddressActivity : BaseActivity(), HasComponent<ManageAddressComponent>, ManageAddressFragment.ManageAddressListener {

    private var binding: ActivityManageAddressBinding? = null

    override fun getComponent(): ManageAddressComponent {
        return DaggerManageAddressComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is ManageAddressFragment -> fragment.setListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageAddressBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initViews()
    }

    private fun initViews() {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            if (!intent.hasExtra(PARAM_SOURCE)) {
                intent.putExtra(PARAM_SOURCE, ManageAddressSource.NOTIFICATION.source)
            }

            bundle.putAll(intent.extras)
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, ManageAddressFragment.newInstance(bundle)).commit()
        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setAddButtonOnClickListener(onClick: () -> Unit) {
        binding?.btnAdd?.setOnClickListener {
            onClick()
        }
    }

    override fun setSearch(query: String, saveAddressDataModel: SaveAddressDataModel?) {
        val mainAddressFragment = getMainAddressFragment()
        mainAddressFragment?.performSearch(query, saveAddressDataModel)
    }

    override fun setToolbarTitle(title: String, isBtnAddVisible: Boolean) {
        binding?.tvTitle?.text = title
        binding?.btnAdd?.apply {
            if (isBtnAddVisible) {
                visible()
            } else {
                gone()
            }
        }
    }

    private fun getMainAddressFragment(): MainAddressFragment? {
        val fragments = supportFragmentManager.fragments
        fragments.forEach { currentFragment ->
            if (currentFragment != null && currentFragment.isVisible && currentFragment is MainAddressFragment) {
                return currentFragment
            }
        }
        return null
    }
}
