package com.tokopedia.topchat.chatlist.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.topchat.chatlist.adapter.InboxChatAdapter;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatlist.presenter.WebSocketInterface;

import java.util.List;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatContract {

    public interface View extends CustomerView {

        String getNav();

        void enableActions();

        RefreshHandler getRefreshHandler();

        InboxChatAdapter getAdapter();

        void finishLoading();

        void setMustRefresh(boolean b);

        void removeError();

        void disableActions();

        void finishContextMode();

        boolean hasRetry();

        Context getActivity();

        void startActivity(Intent instance);

        void startActivityForResult(Intent intent, int openDetailMessage);

        void overridePendingTransition(int i, int i1);

        String getFilter();

        String getKeyword();

        void showError(String localizedMessage);

        Bundle getArguments();

        void moveViewToTop();

        void setOptionsMenuFromSelect();

        void finishSearch();

        void removeList(List<Pair> originList, List<DeleteChatViewModel> list);

        void setResultSearch(InboxChatViewModel inboxChatViewModel);

        void setResultFetch(InboxChatViewModel messageData);

        Context getContext();

        WebSocketInterface getInterface();

        void notifyConnectionWebSocket();

        void showErrorWarningDelete(int maxMessageDelete);

        void showErrorFull(String errorMessage);

        void dropKeyboard();

        void onErrorDeleteMessage(String errorMessage);

        void setMenuEnabled(boolean b);

        void saveResult();

        void reloadNotifDrawer();

        void handleBroadcastChatMetaData(TopChatBlastSellerMetaData topChatBlastSellerMetaData);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void createWebSocket();

        void resetAttempt();


        void closeWebsocket();

    }
}
