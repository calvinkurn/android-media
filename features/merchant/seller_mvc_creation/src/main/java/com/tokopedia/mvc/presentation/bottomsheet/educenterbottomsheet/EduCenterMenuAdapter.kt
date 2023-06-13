package com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.model.EduCenterMenuModel
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.UrlConstant.URL_MAIN_ARTICLE

class EduCenterMenuAdapter(val context: Context,
                           val listener: EduCenterClickListener) : RecyclerView.Adapter<EduCenterMenuViewHolder>()  {

    private var educationCenterMenuList = arrayListOf(
        EduCenterMenuModel(context.resources?.getString(R.string.smvc_edu_center_about_my_coupon)),
        EduCenterMenuModel(context.resources?.getString(R.string.smvc_edu_center_read_article), URL_MAIN_ARTICLE)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EduCenterMenuViewHolder {
        return EduCenterMenuViewHolder(
            LayoutInflater.from(context).inflate(EduCenterMenuViewHolder.LAYOUT, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: EduCenterMenuViewHolder, position: Int) {
        holder.bind(educationCenterMenuList[position])
    }

    override fun getItemCount(): Int {
        return educationCenterMenuList.size
    }

}
