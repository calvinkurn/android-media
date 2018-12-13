package com.tokopedia.topchat.chatroom.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.chatRoomSettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface;
import com.tokopedia.topchat.common.InboxChatConstant;
import com.tokopedia.topchat.common.di.DaggerChatRoomComponent;
import com.tokopedia.topchat.common.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class ChatRoomSettingsActivity extends BaseSimpleActivity implements ChatSettingsInterface.View, CompoundButton.OnCheckedChangeListener {

    private Switch chatPromotionSwitch, chatPersonalSwitch;
    private ConstraintLayout chatPromotionInfoView, chatPersonalInfoView;
    private CardView chatPersonalCardView, chatPromotionalcardView;
    private TextView chatPromotionInfoText, chatPersonalInfoText;
    public static final int RESULT_CODE_CHAT_SETTINGS_ENABLED = 1;
    public static final int RESULT_CODE_CHAT_SETTINGS_DISABLED = 2;
    private ChatSettingsResponse chatSettingsResponse;
    private boolean isChatEnabled;
    private String chatRole, senderName;
    private boolean shouldShowToast = false;
    private FrameLayout progressBarLayout;


    @Inject
    ChatSettingsInterface.Presenter chatSettingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInjector();
        chatSettingsPresenter.attachView(this);
        this.chatSettingsResponse = getIntent().getParcelableExtra(InboxChatConstant.CHATRESPONSEMODEL);
        isChatEnabled = getIntent().getBooleanExtra(InboxChatConstant.CHAT_ENABLED, true);
        chatRole = getIntent().getStringExtra(InboxChatConstant.CHAT_ROLE);
        senderName = getIntent().getStringExtra(InboxChatConstant.SENDER_NAME);

        initView();
        setChatSettingsVisibility(chatRole);
        toolbar.setTitle(getString(R.string.chat_incoming_settings) + " " + senderName);
        if (chatSettingsResponse != null) {
            chatSettingsPresenter.initialChatSettings(this.chatSettingsResponse);
        }
        if (isChatEnabled) {
            chatSettingsPresenter.onPersonalChatSettingChange(true, false);
            chatSettingsPresenter.onPromotionalChatSettingChange(true, false);
        }

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
        chatPersonalCardView = findViewById(R.id.chat_personal_cardview);
        chatPromotionalcardView = findViewById(R.id.chat_promotional_cardview);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        chatPersonalSwitch.setOnCheckedChangeListener(this);
        chatPromotionSwitch.setOnCheckedChangeListener(this);
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
                    setPromotionalInfoViewVisibility(true);
                } else if (!chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked()) {
                    setPersonalInfoViewVisibility(false);
                    setPromotionalInfoViewVisibility(false);
                }
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
            }
        }
    }

    @Override
    public void updateChatSettingResponse(ChatSettingsResponse chatSettingsResponse) {
        this.chatSettingsResponse = chatSettingsResponse;
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
        if (!isVisible) {
            chatPersonalSwitch.setChecked(true);
            chatPersonalInfoView.setVisibility(View.GONE);
        } else {
            chatPersonalSwitch.setChecked(false);
            chatPersonalInfoView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())) {
                SpannableString str = getInformationText(String.format(getString(R.string.chat_personal_blocked_validity), senderName, Utils.getDateTime(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())), senderName);
                chatPersonalInfoText.setText(str);
            }
        }
    }

    @Override
    public void setPromotionalInfoViewVisibility(boolean isVisible) {
        if (!isVisible) {
            chatPromotionSwitch.setChecked(true);
            chatPromotionInfoView.setVisibility(View.GONE);
        } else {
            chatPromotionSwitch.setChecked(false);
            chatPromotionInfoView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())) {
                SpannableString str = getInformationText(String.format(getString(R.string.chat_promotion_blocked_validity), senderName, Utils.getDateTime(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())), senderName);

                chatPromotionInfoText.setText(str);
            }

        }
    }

    @Override
    public void setChatSettingsVisibility(String chatRole) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) chatPromotionalcardView.getLayoutParams();
        if (!TextUtils.isEmpty(chatRole)) {
            if (chatRole.equalsIgnoreCase(InboxChatConstant.OFFICIAL_TAG)) {
                setPromotionalInfoViewVisibility(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked());
                chatPromotionalcardView.setVisibility(View.VISIBLE);
            } else if (chatRole.equalsIgnoreCase(InboxChatConstant.SELLER_TAG)) {
                layoutParams.setMargins(0, (int)getResources().getDimension(R.dimen.dp_24), 0, 0);
                chatPromotionalcardView.requestLayout();
                chatPromotionalcardView.setVisibility(View.VISIBLE);
                chatPersonalCardView.setVisibility(View.VISIBLE);
                setPersonalInfoViewVisibility(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked());
                setPromotionalInfoViewVisibility(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked());
            } else {
                setPersonalInfoViewVisibility(this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked());
                chatPersonalCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showToastMessage() {
        shouldShowToast = true;
    }

    @Override
    public void showProgressBar() {
        if (progressBarLayout != null) {
            progressBarLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (progressBarLayout != null) {
            progressBarLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (this.chatSettingsResponse != null && this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked()) {
            chatPromotionSwitch.setChecked(false);
        }

        if (compoundButton.getId() == R.id.chat_personal_switch) {
            chatSettingsPresenter.onPersonalChatSettingChange(isChecked, true);
//            showPersonalToast(isChecked);
        } else if (compoundButton.getId() == R.id.chat_promotion_switch) {
            chatSettingsPresenter.onPromotionalChatSettingChange(isChecked, true);
//            showPromotionToast(isChecked);
        }
    }

    private SpannableString getInformationText(String text, String senderName) {
        final SpannableString sb = new SpannableString(text);

        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        int startIndex = text.indexOf(senderName);
        sb.setSpan(bss, startIndex, startIndex + senderName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    @Override
    public void showPersonalToast(boolean enable) {
        if (shouldShowToast) {
            if (!enable) {
                ToasterNormal.show(this, String.format(getString(R.string.enable_chat_personal_settings), senderName));
            } else {
                ToasterNormal.show(this, String.format(getString(R.string.disable_chat_personal_settings), senderName));
            }
        }
    }


    @Override
    public void showPromotionToast(boolean enable) {
        if (shouldShowToast) {
            if (enable && this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked()) {
                ToasterError.make(findViewById(android.R.id.content), getString(R.string.enable_chat_promotion_blocked_settings), BaseToaster.LENGTH_LONG).show();
            } else if (!enable) {
                ToasterNormal.show(this, String.format(getString(R.string.enable_chat_promotion_settings), senderName));
            } else {
                ToasterNormal.show(this, String.format(getString(R.string.disable_chat_promotion_settings), senderName));
            }
        }
    }
}

