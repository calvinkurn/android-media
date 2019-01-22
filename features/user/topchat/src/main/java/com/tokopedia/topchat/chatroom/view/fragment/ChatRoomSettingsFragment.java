package com.tokopedia.topchat.chatroom.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.view.activity.ChatRoomActivity;
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface;
import com.tokopedia.topchat.common.InboxChatConstant;
import com.tokopedia.topchat.common.di.DaggerChatRoomComponent;
import com.tokopedia.topchat.common.util.Utils;

import javax.inject.Inject;

public class ChatRoomSettingsFragment extends BaseDaggerFragment implements ChatSettingsInterface.View, CompoundButton.OnCheckedChangeListener{

    private Switch chatPromotionSwitch, chatPersonalSwitch;
    private ConstraintLayout chatPromotionInfoView, chatPersonalInfoView;
    private CardView chatPersonalCardView, chatPromotionalcardView;
    private TextView chatPromotionInfoText, chatPersonalInfoText;
    public static final int RESULT_CODE_CHAT_SETTINGS_ENABLED = 1;
    public static final int RESULT_CODE_CHAT_SETTINGS_DISABLED = 2;
    private ChatSettingsResponse chatSettingsResponse;
    private boolean isChatEnabled;
    private String messageId;
    private String chatRole, senderName;
    private boolean shouldShowToast = false;
    private FrameLayout progressBarLayout;
    private ConstraintLayout promotionConstraintLayout;

    @Inject
    ChatSettingsInterface.Presenter chatSettingsPresenter;


    public static ChatRoomSettingsFragment createInstance(Bundle extras) {
        ChatRoomSettingsFragment fragment = new ChatRoomSettingsFragment();
        fragment.setArguments(extras);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            messageId = getArguments().getString(ChatRoomActivity.PARAM_MESSAGE_ID);
            this.chatSettingsResponse = getArguments().getParcelable(InboxChatConstant.CHATRESPONSEMODEL);
            isChatEnabled = getArguments().getBoolean(InboxChatConstant.CHAT_ENABLED, true);
            chatRole = getArguments().getString(InboxChatConstant.CHAT_ROLE);
            senderName = getArguments().getString(InboxChatConstant.SENDER_NAME);
        }
        chatSettingsPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chat_settings, container, false);

        chatPersonalSwitch = rootView.findViewById(R.id.chat_personal_switch);
        chatPromotionSwitch = rootView.findViewById(R.id.chat_promotion_switch);
        chatPromotionInfoView = rootView.findViewById(R.id.chat_promotion_layout);
        chatPersonalInfoView = rootView.findViewById(R.id.chat_personal_layout);
        chatPromotionInfoText = rootView.findViewById(R.id.chat_promotion_info);
        chatPersonalInfoText = rootView.findViewById(R.id.chat_personal_info);
        chatPersonalCardView = rootView.findViewById(R.id.chat_personal_cardview);
        chatPromotionalcardView = rootView.findViewById(R.id.chat_promotional_cardview);
        progressBarLayout = rootView.findViewById(R.id.progress_bar_layout);
        promotionConstraintLayout = rootView.findViewById(R.id.cl_promotionView);
        chatPersonalSwitch.setOnCheckedChangeListener(this);
        chatPromotionSwitch.setOnCheckedChangeListener(this);

        setChatSettingsVisibility(chatRole);
        if (chatSettingsResponse != null) {
            chatSettingsPresenter.initialChatSettings(this.chatSettingsResponse);
        }
        if (isChatEnabled) {
            chatSettingsPresenter.onPersonalChatSettingChange(messageId,true, false);
            chatSettingsPresenter.onPromotionalChatSettingChange(messageId,true, false);
        } else if (chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked()) {
            setPromotionViewOpacity(true);
        }


        return rootView;
    }

    @Override
    protected void initInjector() {
        DaggerChatRoomComponent.builder().appComponent(((MainApplication) getActivity().getApplication()).getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
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
                getActivity().setResult(chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked() ? RESULT_CODE_CHAT_SETTINGS_DISABLED
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
        setPersonalSwitchState(chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked());
        setPromotionSwitchState(chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked());
    }

    @Override
    public Context getAppContext() {
        if (getActivity() != null)
            return getActivity().getApplicationContext();
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
                layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.dp_24), 0, 0);
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
            chatSettingsPresenter.onPersonalChatSettingChange(messageId, isChecked, true);
        } else if (compoundButton.getId() == R.id.chat_promotion_switch) {
            chatSettingsPresenter.onPromotionalChatSettingChange(messageId, isChecked, true);
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
    public void setPersonalToast(boolean enable) {
        if (shouldShowToast) {
            if (!enable) {
                ToasterNormal.show(getActivity(), String.format(getString(R.string.enable_chat_personal_settings), senderName));
            } else {
                ToasterNormal.show(getActivity(), String.format(getString(R.string.disable_chat_personal_settings), senderName));
            }
        }
    }


    @Override
    public void setPromotionToast(boolean enable) {
        if (shouldShowToast) {
            if (enable && this.chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked()) {
                ToasterError.make(getView(), getString(R.string.enable_chat_promotion_blocked_settings), BaseToaster.LENGTH_LONG).show();
            } else if (!enable) {
                ToasterNormal.show(getActivity(), String.format(getString(R.string.enable_chat_promotion_settings), senderName));
            } else {
                ToasterNormal.show(getActivity(), String.format(getString(R.string.disable_chat_promotion_settings), senderName));
            }
        }
    }

    @Override
    public void setPromotionViewOpacity(boolean enable) {
        if (chatPromotionalcardView.getVisibility() == View.VISIBLE && chatPersonalCardView.getVisibility() == View.VISIBLE) {
            if (enable) {
                promotionConstraintLayout.setAlpha(0.4f);
            } else {
                promotionConstraintLayout.setAlpha(1.0f);
            }
        }
    }

    @Override
    public void showErrorMessage() {
        ToasterNormal.show(getActivity(), getResources().getString(R.string.error_chat_message));
    }

    @Override
    public String getQueryString(int id) {
        return GraphqlHelper.loadRawString(getResources(), id);
    }

    private void setPersonalSwitchState(boolean enable) {
        chatPersonalSwitch.setChecked(!enable);
    }


    private void setPromotionSwitchState(boolean enable) {
        chatPromotionSwitch.setChecked(!enable);
    }
}
