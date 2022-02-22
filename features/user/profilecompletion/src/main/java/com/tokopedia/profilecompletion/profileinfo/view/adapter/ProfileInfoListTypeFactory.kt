package com.tokopedia.profilecompletion.profileinfo.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.DividerProfileUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoItemUiModel
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoTitleUiModel
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.DividerProfileViewHolder
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoItemViewHolder
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoTitleViewHolder

class ProfileInfoListTypeFactory(
    private val profileItemListener: ProfileInfoItemViewHolder.ProfileInfoItemInterface,
    private val profileItemTitleListener: ProfileInfoTitleViewHolder.ProfileInfoTitleInterface
): BaseAdapterTypeFactory(), TypeFactoryProfileViewHolder {
    override fun type(profileInfoItemUiModel: ProfileInfoItemUiModel): Int {
	return ProfileInfoItemViewHolder.LAYOUT_RES
    }

    override fun type(profileInfoTitleUiModel: ProfileInfoTitleUiModel): Int {
        return ProfileInfoTitleViewHolder.LAYOUT_RES
    }

    override fun type(dividerProfileUiModel: DividerProfileUiModel): Int {
        return DividerProfileViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProfileInfoItemViewHolder.LAYOUT_RES -> ProfileInfoItemViewHolder(parent, profileItemListener)
            ProfileInfoTitleViewHolder.LAYOUT_RES -> ProfileInfoTitleViewHolder(parent, profileItemTitleListener)
            DividerProfileViewHolder.LAYOUT_RES -> DividerProfileViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}