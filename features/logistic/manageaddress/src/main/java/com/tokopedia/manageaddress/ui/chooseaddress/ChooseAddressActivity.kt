package com.tokopedia.manageaddress.ui.chooseaddress

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.manageaddress.di.manageaddress.DaggerManageAddressComponent
import com.tokopedia.manageaddress.di.manageaddress.ManageAddressComponent

class ChooseAddressActivity: BaseSimpleActivity(), HasComponent<ManageAddressComponent> {
    override fun getNewFragment(): Fragment? {
        return ChooseAddressFragment()
    }

    override fun getComponent(): ManageAddressComponent {
        return DaggerManageAddressComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        fun newInstance(context: Context): Intent =
                Intent(context, ChooseAddressActivity::class.java)

    }
}