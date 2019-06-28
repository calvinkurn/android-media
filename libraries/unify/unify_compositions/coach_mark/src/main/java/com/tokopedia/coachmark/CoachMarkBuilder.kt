/*
 * Copyright 2019 Tokopedia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Hendy
 * Edited by Meyta
 */

package com.tokopedia.coachmark

import android.os.Parcel
import android.os.Parcelable

class CoachMarkBuilder : Parcelable {

    var layoutRes: Int = 0
        private set
    var spacingRes: Int = 0
        private set
    var circleIndicatorBackgroundDrawableRes: Int = 0
        private set

    fun customView(customViewRes: Int): CoachMarkBuilder {
        this.layoutRes = customViewRes
        return this
    }

    fun spacingRes(spacingRes: Int): CoachMarkBuilder {
        this.spacingRes = spacingRes
        return this
    }

    fun circleIndicatorBackgroundDrawableRes(circleIndicatorBackgroundDrawableRes: Int): CoachMarkBuilder {
        this.circleIndicatorBackgroundDrawableRes = circleIndicatorBackgroundDrawableRes
        return this
    }

    constructor() {}

    fun build(): CoachMark {
        return CoachMark.newInstance(this)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.layoutRes)
        dest.writeInt(this.spacingRes)
        dest.writeInt(this.circleIndicatorBackgroundDrawableRes)
    }

    protected constructor(`in`: Parcel) {
        this.layoutRes = `in`.readInt()
        this.spacingRes = `in`.readInt()
        this.circleIndicatorBackgroundDrawableRes = `in`.readInt()
    }

    companion object CREATOR : Parcelable.Creator<CoachMarkBuilder> {
        override fun createFromParcel(parcel: Parcel): CoachMarkBuilder {
            return CoachMarkBuilder(parcel)
        }

        override fun newArray(size: Int): Array<CoachMarkBuilder?> {
            return arrayOfNulls(size)
        }
    }
}
