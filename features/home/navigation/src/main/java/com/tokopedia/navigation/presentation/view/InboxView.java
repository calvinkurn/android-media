package com.tokopedia.navigation.presentation.view;

import com.tokopedia.navigation.domain.model.Inbox;

import java.util.List;

/**
 * Created by meta on 25/07/18.
 */
public interface InboxView extends LoadDataView {

    void onRenderInboxList(List<Inbox> inboxList);

}
