package com.tokopedia.officialstore.category.presentation.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.unifyprinciples.Typography.Companion.BOLD
import kotlinx.android.synthetic.main.view_official_store_category.view.*

class OfficialHomeContainerAdapter(
        val context: Context?,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    private val MAX_CHAR_CATEGORY_NAME = 12
    private val registeredFragment = SparseArrayCompat<Fragment>()

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
        val view = LayoutInflater.from(context).inflate(R.layout.view_official_store_category,null)
        with(view){
            ImageHandler.loadImage(
                    context,
                    image_view_category_icon,
                    categoryList[position].icon,
                    R.drawable.ic_loading_image
            )

            var categoryName = categoryList[position].title
            val maxLength = if (categoryName.length < MAX_CHAR_CATEGORY_NAME)
                categoryName.length else MAX_CHAR_CATEGORY_NAME
            categoryName = categoryName.substring(0, maxLength)
            text_view_category_title.text = if (maxLength == MAX_CHAR_CATEGORY_NAME) "${categoryName.trim()}..." else categoryName
        }
        return view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragment.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragment.remove(position)
        super.destroyItem(container, position, `object`)
    }
}