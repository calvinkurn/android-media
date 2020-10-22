package com.tokopedia.topads.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import kotlinx.android.synthetic.main.activity_ad_type_selection.*
import kotlinx.android.synthetic.main.item_topads_added_ads.view.*

class AdTypeSelectionActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_ad_type_selection
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topads_ad_card1.run {
            card_icon.setImageDrawable(context.getResDrawable(R.drawable.ic_topads_added_ads_produk))
            card_title.text = getString(R.string.topads_create_ad_product_type_selection_title)
            card_subtitle.text = getString(R.string.topads_create_ad_product_type_selection_subtitle)
            setOnClickListener {

            }
        }
        topads_ad_card2.run {
            card_icon.setImageDrawable(context.getResDrawable(R.drawable.ic_topads_added_ads_headline))
            card_title.text = getString(R.string.topads_create_ad_headline_type_selection_title)
            card_subtitle.text = getString(R.string.topads_create_ad_headline_type_selection_subtitle)
            setOnClickListener{

            }
        }
    }
}