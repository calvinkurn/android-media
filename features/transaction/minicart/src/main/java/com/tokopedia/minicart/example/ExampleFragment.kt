package com.tokopedia.minicart.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

class ExampleFragment : Fragment(), MiniCartWidgetListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shopIds = listOf("480552")
        val miniCartWidget = view.findViewById<MiniCartWidget>(R.id.mini_cart_widget)
        miniCartWidget.initialize(shopIds, this, this)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        activity?.let {
            Toast.makeText(it, this.javaClass.name + "  - onCartItemsUpdated", Toast.LENGTH_SHORT).show()
        }
    }
}