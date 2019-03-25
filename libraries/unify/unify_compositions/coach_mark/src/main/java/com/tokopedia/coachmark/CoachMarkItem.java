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
 * Created by Hendy on 4/13/2017
 * Edited by Meyta
 */

package com.tokopedia.coachmark;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

public class CoachMarkItem {

    private View view;
    private String title;
    private String description;
    private CoachMarkContentPosition coachMarkContentPosition;
    private int tintBackgroundColor;
    private ViewGroup scrollView;

    public CoachMarkItem(@Nullable View view, @Nullable String title, String description) {
        this(view, title, description, CoachMarkContentPosition.UNDEFINED);
    }

    public CoachMarkItem(@Nullable View view, @Nullable String title,
                         String description, CoachMarkContentPosition coachMarkContentPosition) {
        this(view, title, description, coachMarkContentPosition, 0);
    }

    public CoachMarkItem(@Nullable View view, @Nullable String title,
                         String description, CoachMarkContentPosition coachMarkContentPosition,
                         int tintBackgroundColor) {
        this(view, title, description, coachMarkContentPosition, tintBackgroundColor, null);
    }

    public CoachMarkItem(@Nullable View view, @Nullable String title,
                         String description, CoachMarkContentPosition coachMarkContentPosition,
                         int tintBackgroundColor, ViewGroup scrollView) {
        this.view = view;
        this.title = title;
        this.description = description;
        this.coachMarkContentPosition = coachMarkContentPosition;
        this.tintBackgroundColor = tintBackgroundColor;
        this.scrollView = scrollView;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getTintBackgroundColor() {
        return tintBackgroundColor;
    }

    public CoachMarkContentPosition getCoachMarkContentPosition() {
        return coachMarkContentPosition;
    }

    private int[] location;
    private int radius;
    public CoachMarkItem withCustomTarget(int[] location, int radius){
        if (location.length != 2) {
            return this;
        }
        this.location = location;
        this.radius = radius;
        return this;
    }

    public CoachMarkItem withCustomTarget(int[] location){
        if (location.length!= 4) {
            return this;
        }
        this.location = location;
        return this;
    }

    public int[] getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public View getView() {
        return view;
    }
    public ViewGroup getScrollView() {
        return scrollView;
    }
}