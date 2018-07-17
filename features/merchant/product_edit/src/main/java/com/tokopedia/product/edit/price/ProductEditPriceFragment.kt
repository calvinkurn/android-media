package com.tokopedia.product.edit.price

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.design.bottomsheet.BottomSheetView
import com.tokopedia.product.edit.R
import kotlinx.android.synthetic.main.fragment_product_edit_price.*

class ProductEditPriceFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerCounterInputViewPrice.spinnerTextView.editText.setOnClickListener({
            val bottomSheetView = BottomSheetView(context!!)
            bottomSheetView.renderBottomSheet(BottomSheetView.BottomSheetField.BottomSheetFieldBuilder()
                    .setTitle(getString(R.string.product_label_add_maksimum_buy))
                    .setBody(getString(R.string.product_label_add_maksimum_buy))
                    .setImg(R.drawable.ic_check_video_featured)
                    .build())
            bottomSheetView.show()
        })

        textAddMaksimumBuy.setOnClickListener({
            textInputLayoutMaksimumBuy.visibility = View.VISIBLE
            textAddMaksimumBuy.visibility = View.GONE
        })
    }

    companion object {

        fun createInstance(): Fragment {
            return ProductEditPriceFragment()
        }
    }
}
