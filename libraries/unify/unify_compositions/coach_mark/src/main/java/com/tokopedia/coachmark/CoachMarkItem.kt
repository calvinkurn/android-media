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
 * Created by Hendry on 4/13/2017
 * Edited by Meyta
 */

package com.tokopedia.coachmark

import android.view.View
import android.view.ViewGroup

class CoachMarkItem @JvmOverloads constructor(val view: View?,
                                val title: String?,
                                val description: String,
                                val coachMarkContentPosition: CoachMarkContentPosition = CoachMarkContentPosition.UNDEFINED,
                                val tintBackgroundColor: Int = 0,
                                val scrollView: ViewGroup? = null) {

    var location: IntArray? = null
        private set

    var radius: Int = 0
        private set

    fun withCustomTarget(location: IntArray, radius: Int): CoachMarkItem {
        if (location.size != 2) {
            return this
        }
        this.location = location
        this.radius = radius
        return this
    }

    fun withCustomTarget(location: IntArray): CoachMarkItem {
        if (location.size != 4) {
            return this
        }
        this.location = location
        return this
    }
}
