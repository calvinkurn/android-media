package com.tokopedia.tkpd.home.banner.holder;

import android.support.v4.app.Fragment;

public interface Holder<T>{
    Fragment createFragment(T data);
}