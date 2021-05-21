package com.tokopedia.minicart.example

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

class ExampleFragment : Fragment(), MiniCartWidgetListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val miniCartWidget = MiniCartWidget(requireContext())
        this.activity?.let {
            miniCartWidget.initialize(listOf("1"), it, this)
        }
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        activity?.let {
            Toast.makeText(it, this.javaClass.name + "  - onCartItemsUpdated", Toast.LENGTH_SHORT).show()
        }
    }
}