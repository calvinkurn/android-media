package com.tokopedia.productcard.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import kotlinx.android.synthetic.main.product_card_options_fragment_layout.*


internal class ProductCardOptionsFragment: TkpdBaseV4Fragment() {

    override fun getScreenName(): String {
        return "product card options"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_card_options_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { activity ->
            val optionItem1 = ProductCardOptionsItemModel("Simpan ke Wishlist")
            val optionItemView1 = ProductCardOptionsItemView(activity).also { it.setOption(optionItem1) { optionItem, _ ->
                    Toast.makeText(activity, optionItem.title, Toast.LENGTH_SHORT).show()
                }
            }

            productCardOptionsBottomSheet?.addView(optionItemView1)
            View.inflate(activity, R.layout.product_card_optionos_item_divider, productCardOptionsBottomSheet)

            val optionItem2 = ProductCardOptionsItemModel("Lihat Produk Serupa")
            val optionItemView2 = ProductCardOptionsItemView(activity).also { it.setOption(optionItem2) { optionItem, _ ->
                    Toast.makeText(activity, optionItem.title, Toast.LENGTH_SHORT).show()
                }
            }

            productCardOptionsBottomSheet?.addView(optionItemView2)
        }
    }
}