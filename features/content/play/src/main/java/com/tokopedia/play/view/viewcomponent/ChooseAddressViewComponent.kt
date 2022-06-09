package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 03/06/22
 */
class ChooseAddressViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_choose_address_widget) {

    private val chooseAddressWidget: ChooseAddressWidget = findViewById(R.id.widget_choose_addres)

    private val insideListener = object : ChooseAddressWidget.ChooseAddressWidgetListener{
        override fun onLocalizingAddressUpdatedFromWidget() {
            chooseAddressWidget.updateWidget()
            listener.onAddressUpdated(this@ChooseAddressViewComponent)
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
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()
    }

    interface Listener {
        fun getFragmentForAddress(view: ChooseAddressViewComponent) : Fragment
        fun onAddressUpdated(view: ChooseAddressViewComponent)
    }
}