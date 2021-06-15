package com.tokopedia.manageaddress.ui.manageaddress

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.DaggerManageAddressComponent
import com.tokopedia.manageaddress.di.ManageAddressComponent
import kotlinx.android.synthetic.main.activity_manage_address.*

class ManageAddressActivity : BaseActivity(), HasComponent<ManageAddressComponent>, ManageAddressFragment.ManageAddressListener {

    override fun getComponent(): ManageAddressComponent {
        return DaggerManageAddressComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is ManageAddressFragment){
            fragment.setListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_address)
        initViews()
    }

    private fun initViews() {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, ManageAddressFragment.newInstance(bundle)).commit()
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setAddButtonOnClickListener(onClick: () -> Unit) {
        btn_add.setOnClickListener {
            onClick()
        }
    }
}