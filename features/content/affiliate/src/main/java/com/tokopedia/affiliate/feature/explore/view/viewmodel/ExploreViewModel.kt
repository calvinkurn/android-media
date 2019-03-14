package com.tokopedia.affiliate.feature.explore.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory

/**
 * @author by yfsx on 24/09/18.
 */
data class ExploreViewModel(
        var exploreCardViewModel: ExploreCardViewModel = ExploreCardViewModel()
) : Visitable<ExploreTypeFactory>, Parcelable {

    constructor(source: Parcel) : this(
            source.readParcelable<ExploreCardViewModel>(
                    ExploreCardViewModel::class.java.classLoader
            ) ?: ExploreCardViewModel()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(exploreCardViewModel, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ExploreViewModel> = object : Parcelable.Creator<ExploreViewModel> {
            override fun createFromParcel(source: Parcel): ExploreViewModel = ExploreViewModel(source)
            override fun newArray(size: Int): Array<ExploreViewModel?> = arrayOfNulls(size)
        }
    }

    override fun type(typeFactory: ExploreTypeFactory?): Int = typeFactory!!.type(this)
}
