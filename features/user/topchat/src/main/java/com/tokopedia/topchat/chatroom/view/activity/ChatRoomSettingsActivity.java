package com.tokopedia.topchat.chatroom.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.chatRoomSettings.ChatBlockResponse;
import com.tokopedia.topchat.chatroom.domain.pojo.chatRoomSettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface;
import com.tokopedia.topchat.common.di.DaggerChatRoomComponent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

public class ChatRoomSettingsActivity extends BaseSimpleActivity implements ChatSettingsInterface.View, CompoundButton.OnCheckedChangeListener {
    private Switch chatPromotionSwitch, chatPersonalSwitch;
    private ConstraintLayout chatPromotionInfoView, chatPersonalInfoView;
    private TextView chatPromotionInfoText, chatPersonalInfoText;
    public static final int RESULT_CODE_CHAT_SETTINGS_ENABLED = 1;
    public static final int RESULT_CODE_CHAT_SETTINGS_DISABLED = 2;
    private Locale mLocale;
    private ChatSettingsResponse chatSettingsResponse;


    @Inject
    ChatSettingsInterface.Presenter chatSettingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInjector();
        chatSettingsPresenter.attachView(this);
        this.chatSettingsResponse = getIntent().getParcelableExtra("ChatResponseModel");
        initView();
    }

    protected void initInjector() {
        DaggerChatRoomComponent.builder().appComponent(((MainApplication) getApplication()).getAppComponent())
                .build()
                .inject(this);
    }

    private void initView() {
        chatPersonalSwitch = findViewById(R.id.chat_personal_switch);
        chatPromotionSwitch = findViewById(R.id.chat_promotion_switch);
        chatPromotionInfoView = findViewById(R.id.chat_promotion_layout);
        chatPersonalInfoView = findViewById(R.id.chat_personal_layout);
        chatPromotionInfoText = findViewById(R.id.chat_promotion_info);
        chatPersonalInfoText = findViewById(R.id.chat_personal_info);
        chatPersonalSwitch.setOnCheckedChangeListener(this);
        chatPromotionSwitch.setOnCheckedChangeListener(this);

        if (this.chatSettingsResponse != null) {
            setPersonalInfoViewVisibility(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked());
            setPromotionalInfoViewVisibility(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked());
        }
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat_settings;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void setChatSettingPersonalResponse(ChatSettingsResponse chatSettingsResponse) {
        if (chatSettingsResponse != null) {
            this.chatSettingsResponse = chatSettingsResponse;
            if (chatSettingsResponse.getChatBlockResponse() != null && chatSettingsResponse.getChatBlockResponse().getChatBlockStatus() != null) {
                if (chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked()) {
                    setPersonalInfoViewVisibility(true);
                } else if (!chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked()) {
                    setPersonalInfoViewVisibility(false);
                }
                setPersonalInfoViewVisibility(chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked());
                setResult(chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked() ? RESULT_CODE_CHAT_SETTINGS_DISABLED
                        : RESULT_CODE_CHAT_SETTINGS_ENABLED);
            }
        }
    }

    @Override
    public void setChatSettingPromotionResponse(ChatSettingsResponse chatSettingsResponse) {
        if (chatSettingsResponse != null) {
            this.chatSettingsResponse = chatSettingsResponse;
            if (chatSettingsResponse.getChatBlockResponse() != null && chatSettingsResponse.getChatBlockResponse().getChatBlockStatus() != null) {
                if (chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked()) {
                    setPromotionalInfoViewVisibility(true);
                } else if (!chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked()) {
                    setPromotionalInfoViewVisibility(false);
                }
                setPromotionalInfoViewVisibility(chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked());
            }
        }
    }

    @Override
    public String getMessageId() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(ChatRoomActivity.PARAM_MESSAGE_ID);
        } else return "";
    }

    @Override
    public Context getAppContext() {
        if (this != null)
            return this.getApplicationContext();
        else
            return null;
    }

    @Override
    public void setPersonalInfoViewVisibility(boolean isVisible) {
        if (isVisible) {
            chatPersonalSwitch.setChecked(true);
            chatPersonalInfoView.setVisibility(View.GONE);
        } else {
            chatPersonalSwitch.setChecked(false);
            chatPersonalInfoView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())) {
                chatPersonalInfoText.setText(String.format(getString(R.string.chat_personal_blocked_validity), getDateTime(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())));
            }
        }
    }

    @Override
    public void setPromotionalInfoViewVisibility(boolean isVisible) {
        if (isVisible) {
            chatPromotionSwitch.setChecked(true);
            chatPromotionInfoView.setVisibility(View.GONE);
        } else {
            chatPromotionSwitch.setChecked(false);
            chatPromotionInfoView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())) {
                chatPromotionInfoText.setText(String.format(getString(R.string.chat_promotion_blocked_validity), getDateTime(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())));
            }

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (this.chatSettingsResponse != null && this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked() == isChecked) {
            return;
        }
        if (compoundButton.getId() == R.id.chat_personal_switch) {
            chatSettingsPresenter.onPersonalChatSettingChange(isChecked);
        } else if (compoundButton.getId() == R.id.chat_promotion_switch) {
            chatSettingsPresenter.onPromotionalChatSettingChange(isChecked);
        }
    }

    public String getDateTime(String isoTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX", getLocale());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY", getLocale());
        try {
            Date date = inputFormat.parse(isoTime);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

    }


    private Locale getLocale() {
        if (mLocale == null)
            mLocale = new Locale("in", "ID", "");
        return mLocale;
    }
}

