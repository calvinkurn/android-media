/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package androidx.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Devara
 * There is many component that package-private in RecyclerView, and because that we can't modify recyclerview
 * further
 *
 * With this class, we can "customize" recyclerview behavior
 */
public class CatalogCustomRecyclerView extends RecyclerView {

    public CatalogCustomRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CatalogCustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CatalogCustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OverScroller getOverScroller() {
        return mViewFlinger.mOverScroller;
    }
}
