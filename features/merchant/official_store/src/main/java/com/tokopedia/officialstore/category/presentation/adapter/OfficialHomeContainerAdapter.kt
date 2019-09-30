package com.tokopedia.officialstore.category.presentation.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.presentation.OfficialHomeFragment
import com.tokopedia.officialstore.category.data.model.Category
import kotlinx.android.synthetic.main.official_store_category_view.view.*

class OfficialHomeContainerAdapter(
        val context: Context?,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    var categoryList: MutableList<Category> = mutableListOf()

    private fun getBundle(position: Int): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(OfficialHomeFragment.BUNDLE_CATEGORY,
                categoryList[position])
        return bundle
    }

    override fun getItem(position: Int): Fragment {
        return OfficialHomeFragment.newInstance(getBundle(position))
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categoryList[position].title
    }

    override fun getCount(): Int {
        return categoryList.size
    }

    fun getTabCustomView(position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.official_store_category_view,null)
        with(view){
            ImageHandler.loadImage(
                    context,
                    image_view_category_icon,
                    categoryList[position].icon,
                    R.drawable.ic_loading_image
            )
            text_view_category_title.text = categoryList[position].title
        }
        return view
    }

}