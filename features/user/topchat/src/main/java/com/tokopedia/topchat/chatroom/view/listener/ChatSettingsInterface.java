package com.tokopedia.topchat.chatroom.view.listener;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.RawRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse;

public interface ChatSettingsInterface {

    interface View extends CustomerView {

        void setChatSettingPersonalResponse(ChatSettingsResponse chatSettingsResponse);

        void setChatSettingPromotionResponse(ChatSettingsResponse chatSettingsResponse);

        void updateChatSettingResponse(ChatSettingsResponse chatSettingsResponse);

        Context getAppContext();

        void setPersonalInfoViewVisibility(boolean isVisible);

        void setPromotionalInfoViewVisibility(boolean isVisible);

        void setChatSettingsVisibility(String chatRole);

        void showToastMessage();

        void showProgressBar();

        void hideProgressBar();

        void setPersonalToast(boolean enable);

        void setPromotionToast(boolean enable);

        void setPromotionViewOpacity(boolean enable);

        void showErrorMessage();

        String getQueryString(@RawRes int id);
    }

    interface Presenter extends CustomerPresenter<View> {
        void onPersonalChatSettingChange(String messageId, boolean state, boolean initialState);

        void onPromotionalChatSettingChange(String messageId, boolean state, boolean initialState);

        void initialChatSettings(ChatSettingsResponse chatSettingsResponse);

    }

}
