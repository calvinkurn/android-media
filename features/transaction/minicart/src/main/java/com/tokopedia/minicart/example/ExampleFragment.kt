package com.tokopedia.minicart.example

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

class ExampleFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val widget = MiniCartWidget(requireContext())
        widget.initialize(this, object : MiniCartWidgetListener {
            override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {

            }
        })
    }
}