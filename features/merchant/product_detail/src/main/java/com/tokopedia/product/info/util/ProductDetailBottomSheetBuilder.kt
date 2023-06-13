package com.tokopedia.product.info.util

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.databinding.BsProductShopNotesInfoBinding
import com.tokopedia.product.detail.view.util.toDateId
import com.tokopedia.product.info.data.response.ShopNotesData
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by Yehezkiel on 21/10/20
 */
object ProductDetailBottomSheetBuilder {
    private const val PDP_TIME_PUKUL = "pukul"
    private const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"
    private const val DATE_FORMATTER = "dd MMM yyyy, '$PDP_TIME_PUKUL' HH:mm"

    fun getShopNotesBottomSheet(
        context: Context,
        shopNotesData: ShopNotesData
    ): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_shop_notes_info, null)
        val binding = BsProductShopNotesInfoBinding.bind(view)

        bottomSheetUnify.apply {
            setChild(binding.root)
            setTitle(shopNotesData.title)
        }

        binding.apply {
            productShopNotesDate.text = shopNotesData.updateTime toDateId DATE_FORMATTER
            productShopNotesDesc.text = shopNotesData.content.parseAsHtmlLink(context)
        }

        return bottomSheetUnify
    }

    fun openChooseAddressBottomSheet(listener: ChooseAddressBottomSheet.ChooseAddressBottomSheetListener, childFragmentManager: FragmentManager) {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(listener)
        chooseAddressBottomSheet.show(childFragmentManager, SHIPPING_CHOOSE_ADDRESS_TAG)
    }
}
