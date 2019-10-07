package com.tokopedia.autocomplete;


import com.tokopedia.autocomplete.adapter.SearchAdapter;

public interface TabAutoCompleteCallback {
    public void onAdapterReady(int instanceType, SearchAdapter adapter);
}
