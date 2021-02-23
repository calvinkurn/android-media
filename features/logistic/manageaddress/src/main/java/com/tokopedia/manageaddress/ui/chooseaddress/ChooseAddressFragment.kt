package com.tokopedia.manageaddress.ui.chooseaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.manageaddress.ManageAddressComponent

class ChooseAddressFragment: BaseDaggerFragment(), ChooseAddressWidget.ChooseAddressWidgetListener {

    private var chooseAddressWidget: ChooseAddressWidget? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ManageAddressComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_addres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseAddressWidget = view.findViewById(R.id.choose_address_widget)

        chooseAddressWidget?.bindChooseAddress(this)

    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        chooseAddressWidget?.updateWidget()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {
       //
    }

    override fun onLocalizingAddressServerDown() {
        //
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
        //
    }

    override fun getLocalizingAddressHostFragment(): Fragment {
        return this
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return "src"
    }

}