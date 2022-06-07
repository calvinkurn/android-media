package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 03/06/22
 */
class ChooseAddressViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_choose_address_widget) {

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val chooseAddressWidget: ChooseAddressWidget = findViewById(R.id.widget_choose_addres)

    private val userLocalData: LocalCacheModel

    private val insideListener = object : ChooseAddressWidget.ChooseAddressWidgetListener{
        override fun onLocalizingAddressUpdatedFromWidget() {
            chooseAddressWidget.updateWidget()
        }

        override fun onLocalizingAddressUpdatedFromBackground() {
            //TODO("Not yet implemented")
        }

        override fun onLocalizingAddressServerDown() {
            //TODO("Not yet implemented")
        }

        override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
            //TODO("Not yet implemented")
        }

        override fun getLocalizingAddressHostFragment(): Fragment = listener.getFragmentForAddress(this@ChooseAddressViewComponent)

        override fun getLocalizingAddressHostSourceData(): String = "login"

        override fun onLocalizingAddressLoginSuccess() {
            //TODO("Not yet implemented")
        }

    }

    init {
        chooseAddressWidget.bindChooseAddress(insideListener)

        //TODO() use listener, observe on main fragment and Ui state view model
        userLocalData = ChooseAddressUtils.getLocalizingAddressData(context = rootView.context)
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()
    }

    fun getWareHouseId() : String {
    //userLocalData.isOutOfCoverage()
        return userLocalData.warehouses.find {
            it.service_type == "2h"
        }?.warehouse_id.toString()
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    interface Listener {
        fun getFragmentForAddress(view: ChooseAddressViewComponent) : Fragment
    }
}