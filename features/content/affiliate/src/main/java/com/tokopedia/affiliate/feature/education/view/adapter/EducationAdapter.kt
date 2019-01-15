package com.tokopedia.affiliate.feature.education.view.adapter

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View

/**
 * @author by milhamj on 15/01/19.
 */
class EducationAdapter(val list: List<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        LayoutInflater.from(view.context).inflate()
        return false
    }

    override fun getCount(): Int = list.size
}