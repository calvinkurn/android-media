package com.tokopedia.common.travel.domain.subscriber;

public interface AutoCompleteKeywordListener {

    void onTextReceive(String keyword);

    void onError(Throwable e);
}
