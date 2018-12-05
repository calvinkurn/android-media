package com.tokopedia.topchat.chatroom.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.topchat.chatroom.domain.pojo.chatRoomSettings.ChatBlockResponse;
import com.tokopedia.topchat.chatroom.domain.pojo.chatRoomSettings.ChatSettingsResponse;

public interface ChatSettingsInterface {

    interface View extends CustomerView {

        void setChatSettingPersonalResponse(ChatSettingsResponse chatSettingsResponse);

        void setChatSettingPromotionResponse(ChatSettingsResponse chatSettingsResponse);

        void updateChatSettingResponse(ChatSettingsResponse chatSettingsResponse);

        String getMessageId();

        Context getAppContext();

        void setPersonalInfoViewVisibility(boolean isVisible);

        void setPromotionalInfoViewVisibility(boolean isVisible);

        void setChatSettingsVisibility(String chatRole);

        void showToastMessage();

        void showProgressBar();

        void hideProgressBar();

        void showPersonalToast(boolean enable);

        void showPromotionToast(boolean enable);
    }

    interface Presenter extends CustomerPresenter<View> {
        void onPersonalChatSettingChange(boolean state);

        void onPromotionalChatSettingChange(boolean state);

        void initialChatSettings(ChatSettingsResponse chatSettingsResponse);

    }

}
