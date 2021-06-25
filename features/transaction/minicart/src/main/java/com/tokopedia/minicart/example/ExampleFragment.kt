package com.tokopedia.minicart.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

class ExampleFragment : Fragment(), MiniCartWidgetListener {

    companion object {
        const val SHOP_ID_STAGING = "480552"
        const val SHOP_ID_PROD = "11515028"
    }

    private var miniCartWidget: MiniCartWidget? = null
    private val shopIds = listOf(SHOP_ID_PROD)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example, container, false)
    }

    override fun onResume() {
        super.onResume()
        miniCartWidget?.initialize(
                shopIds = shopIds,
                fragment = this,
                listener = this,
                pageName = MiniCartAnalytics.Page.HOME_PAGE
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        miniCartWidget = view.findViewById<MiniCartWidget>(R.id.mini_cart_widget)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        activity?.let {
            Toast.makeText(it, this.javaClass.name + "  - onCartItemsUpdated", Toast.LENGTH_SHORT).show()
        }

        if (miniCartSimplifiedData.isShowMiniCartWidget) {
            miniCartWidget?.show()
        } else {
            miniCartWidget?.gone()
        }
    }
}