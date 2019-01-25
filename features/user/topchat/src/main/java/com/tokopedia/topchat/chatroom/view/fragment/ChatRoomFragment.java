package com.tokopedia.topchat.chatroom.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.attachproduct.analytics.AttachProductAnalytics;
import com.tokopedia.attachproduct.resultmodel.ResultProduct;
import com.tokopedia.attachproduct.view.activity.AttachProductActivity;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.component.Menus;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.attachinvoice.view.activity.AttachInvoiceActivity;
import com.tokopedia.topchat.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatroom.data.ChatWebSocketConstant;
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.domain.pojo.invoicesent.InvoiceLinkAttributePojo;
import com.tokopedia.topchat.chatroom.domain.pojo.invoicesent.InvoiceLinkPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.Attachment;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.WebSocketResponse;
import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.topchat.chatroom.view.activity.ChatMarketingThumbnailActivity;
import com.tokopedia.topchat.chatroom.view.activity.ChatRoomActivity;
import com.tokopedia.topchat.chatroom.view.activity.ChatRoomSettingsActivity;
import com.tokopedia.topchat.chatroom.view.activity.TimeMachineActivity;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomAdapter;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactoryImpl;
import com.tokopedia.topchat.chatroom.view.adapter.QuickReplyAdapter;
import com.tokopedia.topchat.chatroom.view.adapter.ReasonAdapter;
import com.tokopedia.topchat.chatroom.view.customview.ReasonBottomSheet;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface;
import com.tokopedia.topchat.chatroom.view.presenter.ChatRoomPresenter;
import com.tokopedia.topchat.chatroom.view.presenter.WebSocketInterface;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatShopInfoViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.chatactionbubble.ChatActionBubbleViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.mapper.AttachInvoiceMapper;
import com.tokopedia.topchat.chatroom.view.viewmodel.message.MessageViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.rating.ChatRatingViewModel;
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity;
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatAdapter;
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactory;
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactoryImpl;
import com.tokopedia.topchat.common.InboxChatConstant;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.TopChatRouter;
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics;
import com.tokopedia.topchat.common.analytics.TopChatAnalytics;
import com.tokopedia.topchat.common.di.DaggerInboxChatComponent;
import com.tokopedia.topchat.common.util.Events;
import com.tokopedia.topchat.common.util.ImageUploadHandlerChat;
import com.tokopedia.topchat.common.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tokopedia.topchat.chatroom.view.activity.ChatRoomActivity.MESSAGE_ID;
import static com.tokopedia.topchat.chatroom.view.activity.ChatRoomActivity.PARAM_WEBSOCKET;
import static com.tokopedia.topchat.chatroom.view.fragment.ChatRoomSettingsFragment.RESULT_CODE_CHAT_SETTINGS_DISABLED;
import static com.tokopedia.topchat.chatroom.view.fragment.ChatRoomSettingsFragment.RESULT_CODE_CHAT_SETTINGS_ENABLED;

/**
 * Created by stevenfredian on 9/19/17.
 */

public class ChatRoomFragment extends BaseDaggerFragment
        implements ChatRoomContract.View, InboxMessageConstant, InboxChatConstant, WebSocketInterface {

    private static final int REQUEST_CODE_CHAT_IMAGE = 2325;
    public static final int REQUEST_CODE_CHAT_SETTINGS = 2326;
    private static final int MAX_SIZE_IMAGE_PICKER = 5;
    public static final int CHAT_DELETED_RESULT_CODE = 101;
    public static final int CHAT_GO_TO_SHOP_DETAILS_REQUEST = 202;

    private static final String CONTACT_US_PATH_SEGMENT = "toped-contact-us";
    private static final String BASE_DOMAIN_SHORTENED = "tkp.me";
    private static final String APPLINK_SCHEME = "tokopedia";
    private static final String BRANCH_IO_HOST = "tokopedia.link";
    private static final String CONTACT_US_URL_BASE_DOMAIN = TkpdBaseURL.BASE_CONTACT_US;
    private static final String ROLE_SHOP = "shop";
    public static final String TAG = "ChatRoomFragment";

    public static final String STATUS_DESC_KEY = "CHAT_STATUS_DESC";
    public static final String STATUS_KEY = "CHAT_STATUS";
    public static final String IS_FAVORITE_KEY = "IS_FAVORITE";
    public static final String IS_SHOP_KEY = "IS_SHOP";
    private static final long MILIS_TO_SECOND = 1000;
    @Inject
    ChatRoomPresenter presenter;

    @Inject
    ChatSettingsInterface.Presenter chatSettingsPresenter;

    @Inject
    ChatSettingsAnalytics chatSettingsAnalytics;

    @Inject
    SessionHandler sessionHandler;

    private int networkType;
    private RecyclerView recyclerView;
    private RecyclerView templateRecyclerView;
    private RecyclerView rvQuickReply;
    private ProgressBar progressBar;
    private View replyView;
    private ReasonBottomSheet reasonBottomSheet;

    private ImageView avatar;
    private ImageView onlineStatus;
    private TextView user;
    private TextView onlineDesc;
    private TextView label;

    ChatRoomAdapter adapter;
    TemplateChatAdapter templateAdapter;
    QuickReplyAdapter quickReplyAdapter;

    private ChatRoomTypeFactory typeFactory;
    private TemplateChatTypeFactory templateChatFactory;
    private LinearLayoutManager layoutManager;
    private ImageView sendButton;
    private EditText replyColumn;
    private ImageView attachButton;
    private View pickerButton;
    private View maximize;
    private View mainHeader;
    private Toolbar toolbar;
    private Observable<String> replyWatcher;
    private Observable<Boolean> replyIsTyping;
    private int mode;
    private View notifier;
    private ImageButton headerMenuButton;
    private RelativeLayout sendMessageLayout;
    private RelativeLayout chatBlockLayout;
    private TextView enableChatTextView;
    private RelativeLayout mainContent;

    private String title, avatarImage, lastOnline;
    private boolean isOnline = false;

    private boolean uploading;
    private boolean isChatBot;
    private ChatSettingsResponse chatSettingsResponse;
    private boolean isChatEnabled;
    private String role = "";
    private String senderName = "";
    private boolean showChatSettingMenu = false;
    private TextView blockedText;

    public static ChatRoomFragment createInstance(Bundle extras) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATUS_KEY, isOnline);
        outState.putString(STATUS_DESC_KEY, lastOnline);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(InboxMessageConstant.PARAM_SENDER_NAME, "");
            avatarImage = getArguments().getString(InboxMessageConstant.PARAM_SENDER_IMAGE, "");
            role = getArguments().getString(InboxMessageConstant.PARAM_SENDER_TAG, "");
            senderName = getArguments().getString(InboxMessageConstant.PARAM_SENDER_NAME, "");
            boolean isChatBotArguments = getArguments().getString(TkpdInboxRouter.IS_CHAT_BOT,
                    "false").equals("true");
            isChatBot = (getArguments().getBoolean(TkpdInboxRouter.IS_CHAT_BOT, false) ||
                    isChatBotArguments);
        }

        if (savedInstanceState != null) {
            isOnline = savedInstanceState.getBoolean(STATUS_KEY, false);
            lastOnline = savedInstanceState.getString(STATUS_DESC_KEY, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initVar();
        mainContent = getActivity().findViewById(R.id.main);
        toolbar = getActivity().findViewById(R.id.app_bar);
        mainHeader = toolbar.findViewById(R.id.main_header);
        mainHeader.setVisibility(View.INVISIBLE);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = rootView.findViewById(R.id.reply_list);
        templateRecyclerView = rootView.findViewById(R.id.list_template);
        rvQuickReply = rootView.findViewById(R.id.list_quick_reply);
        replyView = rootView.findViewById(R.id.add_comment_area);
        sendButton = rootView.findViewById(R.id.send_but);
        replyColumn = rootView.findViewById(R.id.new_comment);
        sendButton.requestFocus();
        attachButton = rootView.findViewById(R.id.add_url);
        pickerButton = rootView.findViewById(R.id.image_picker);
        maximize = rootView.findViewById(R.id.maximize);
        notifier = rootView.findViewById(R.id.notifier);
        replyWatcher = Events.text(replyColumn);
        headerMenuButton = toolbar.findViewById(R.id.header_menu);
        sendMessageLayout = rootView.findViewById(R.id.send_message_layout);
        chatBlockLayout = rootView.findViewById(R.id.chat_blocked_layout);
        enableChatTextView = rootView.findViewById(R.id.enable_chat_textView);
        blockedText = rootView.findViewById(R.id.blocked_text);
        enableChatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onPersonalChatSettingChange(getArguments().getString(ChatRoomActivity.PARAM_MESSAGE_ID), true);
            }
        });
        headerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetMenu();
            }
        });
        recyclerView.setHasFixedSize(true);
        templateRecyclerView.setHasFixedSize(true);
        presenter.attachView(this);
        uploading = false;
        prepareView();
        initListener();
        return rootView;
    }

    @Override
    public void enableChatSettings() {
        isChatEnabled = true;
        sendMessageLayout.setVisibility(View.VISIBLE);
        chatBlockLayout.setVisibility(View.GONE);
        disableChatSettingst();
        chatSettingsAnalytics.sendEnableChatSettingTracking();
    }

    @Override
    public String getQueryString(int id) {
        return GraphqlHelper.loadRawString(getResources(), id);
    }

    private void prepareView() {

        if (!TextUtils.isEmpty(getArguments().getString(ChatRoomActivity.PARAM_CUSTOM_MESSAGE,
                ""))) {
            String customMessage = "\n" + getArguments().getString(ChatRoomActivity
                    .PARAM_CUSTOM_MESSAGE, "");
            replyColumn.setText(customMessage
            );
        }

        if (isChatBot) {
            attachButton.setVisibility(View.GONE);
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    private void showRetryFor(ImageUploadViewModel model) {
        adapter.showRetryFor(model, true);
    }

    @Override
    public void onRetrySendImage(final ImageUploadViewModel element) {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST);

        bottomSheetBuilder.addItem(InboxMessageConstant.RESEND, R.string.resend, null);
        bottomSheetBuilder.addItem(InboxMessageConstant.DELETE, R.string.delete, null);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case InboxMessageConstant.RESEND:
                                adapter.remove(element);
                                String fileLoc = element.getImageUrl();
                                ImageUploadViewModel temp = generateChatViewModelWithImage(fileLoc);
                                presenter.startUpload(Collections.singletonList(temp), networkType);
                                adapter.addReply(temp);
                                break;
                            case InboxMessageConstant.DELETE:
                                adapter.remove(element);
                                break;
                        }
                    }
                })
                .createDialog();

        bottomSheetDialog.show();
    }

    private void initListener() {

        replyIsTyping = replyWatcher.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.length() > 0;
            }
        });

        replyIsTyping.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean isNotEmpty) {
                if (isNotEmpty) {
                    presenter.setIsTyping(getArguments().getString(ChatRoomActivity
                            .PARAM_MESSAGE_ID));
                    if (needCreateWebSocket()) {
                        maximize.setVisibility(isChatBot ? View.GONE : View.VISIBLE);
                    }
                    pickerButton.setVisibility(isChatBot ? View.VISIBLE : View.GONE);
                    attachButton.setVisibility(View.GONE);
                }
            }
        });

        replyIsTyping.debounce(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        try {
                            presenter.stopTyping(getArguments().getString(ChatRoomActivity
                                    .PARAM_MESSAGE_ID));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        if (needCreateWebSocket()) {
            sendButton.setOnClickListener(generateSendClickListener());
        } else {
            sendButton.setOnClickListener(getSendInitMessage());
        }

        setPickerButton();

        maximize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximize.setVisibility(View.GONE);
                setPickerButton();
            }
        });

        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyColumn.clearFocus();

                UnifyTracking.eventAttachment(getActivity(), TopChatAnalytics.Category.CHAT_DETAIL,
                        TopChatAnalytics.Action.CHAT_DETAIL_ATTACH,
                        TopChatAnalytics.Name.CHAT_DETAIL);

                ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                        new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                        ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, null, true,
                        null, null);
                Intent intent = ImagePickerActivity.getIntent(getContext(), builder);
                startActivityForResult(intent, REQUEST_CODE_CHAT_IMAGE);
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventInsertAttachment(getActivity(), TopChatAnalytics.Category.CHAT_DETAIL,
                        TopChatAnalytics.Action.CHAT_DETAIL_INSERT,
                        TopChatAnalytics.Name.CHAT_DETAIL);
                presenter.getAttachProductDialog(
                        getArguments().getString(ChatRoomActivity
                                .PARAM_SENDER_ID, ""),
                        getArguments().getString(ChatRoomActivity.PARAM_SENDER_NAME, ""),
                        getArguments().getString(ChatRoomActivity.PARAM_SENDER_ROLE, "")
                );
            }
        });
    }

    private View.OnClickListener generateSendClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToBottom();
                if (templateAdapter != null && templateAdapter.getList().size() != 0) {
                    templateRecyclerView.setVisibility(View.VISIBLE);
                }
                presenter.sendMessage(networkType);
                UnifyTracking.sendChat(getActivity(), TopChatAnalytics.Category.CHAT_DETAIL,
                        TopChatAnalytics.Action.CHAT_DETAIL_SEND,
                        TopChatAnalytics.Name.CHAT_DETAIL);


            }
        };
    }

    @Override
    public void startAttachProductActivity(String shopId, String shopName, boolean isSeller) {
        Intent intent = AttachProductActivity.createInstance(getActivity(), shopId, shopName,
                isSeller, AttachProductActivity.SOURCE_TOPCHAT);
        startActivityForResult(intent, AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE);
    }

    private void setPickerButton() {
        if (needCreateWebSocket()) {
            pickerButton.setVisibility(View.VISIBLE);
            attachButton.setVisibility(isChatBot ? View.GONE : View.VISIBLE);
        } else {
            pickerButton.setVisibility(View.GONE);
            attachButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void addTemplateString(String message) {
        String labelCategory = TopChatAnalytics.Category.INBOX_CHAT;
        if (!getArguments().getBoolean(PARAM_WEBSOCKET)) {
            if (getArguments().getString(InboxMessageConstant.PARAM_SENDER_TAG).equals(ChatRoomActivity.ROLE_SELLER)) {
                labelCategory = TopChatAnalytics.Category.SHOP_PAGE;
            }
            if (getArguments().getString(ChatRoomActivity.PARAM_CUSTOM_MESSAGE, "").length() >
                    0) {
                labelCategory = TopChatAnalytics.Category.PRODUCT_PAGE;
            }
        }

        UnifyTracking.eventClickTemplate(getActivity(), labelCategory,
                TopChatAnalytics.Action.TEMPLATE_CHAT_CLICK,
                TopChatAnalytics.Name.INBOX_CHAT);
        String text = replyColumn.getText().toString();
        int index = replyColumn.getSelectionStart();
        replyColumn.setText(String.format("%s %s %s", text.substring(0, index), message, text
                .substring(index)));
        replyColumn.setSelection(message.length() + text.substring(0, index).length() + 1);
    }

    @Override
    public void goToSettingTemplate() {
        Intent intent = TemplateChatActivity.createInstance(getActivity());
        startActivityForResult(intent, 100);
        getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
    }

    @Override
    public void onGoToGallery(Attachment attachment, String fullTime) {

        ArrayList<String> strings = new ArrayList<>();
        strings.add(attachment.getAttributes().getImageUrl());

        ((TopChatRouter) getActivity().getApplication()).openImagePreviewFromChat(getActivity(),
                strings, new ArrayList<String>(), title, fullTime);
    }

    @Override
    public void onGoToImagePreview(String imageUrl, String replyTime) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(imageUrl);

        ((TopChatRouter) getActivity().getApplication()).openImagePreviewFromChat(getActivity(),
                strings, new ArrayList<String>(), title, replyTime);
    }

    @Override
    public void onGoToWebView(String url, String id) {
        if (!TextUtils.isEmpty(url)) {

            Uri uri = Uri.parse(url);
            KeyboardHandler.DropKeyboard(getActivity(), getView());
            if (uri != null && uri.getScheme() != null) {
                boolean isTargetDomainTokopedia
                        = uri.getHost() != null && uri.getHost().endsWith("tokopedia.com");
                boolean isTargetTkpMeAndNotRedirect
                        = (TextUtils.equals(uri.getHost(), BASE_DOMAIN_SHORTENED)
                        && !TextUtils.equals(uri.getEncodedPath(), "/r"));
                boolean isNeedAuthToken = (isTargetDomainTokopedia || isTargetTkpMeAndNotRedirect);

                if (uri.getScheme().equals(APPLINK_SCHEME)) {
                    ((TkpdInboxRouter) getActivity().getApplicationContext())
                            .actionNavigateByApplinksUrl(getActivity(), url, new Bundle());
                } else if (uri.getPathSegments() != null && uri.getPathSegments().contains
                        (CONTACT_US_PATH_SEGMENT)) {
                    Intent intent = ((TopChatRouter) MainApplication
                            .getAppContext())
                            .getHelpPageActivity(getContext(), url, true);
                    startActivity(intent);
                } else if (isChatBot && isNeedAuthToken) {
                    startActivity(ChatMarketingThumbnailActivity.getCallingIntent(getActivity(),
                            URLGenerator.generateURLSessionLoginV4(url, getContext())));
                } else if (isBranchIOLink(url)) {
                    handleBranchIOLinkClick(url);
                } else {
                    ((TopChatRouter) getActivity().getApplication()).openRedirectUrl(getActivity
                            (), url);
                }
            }
        }
    }

    @Override
    public void handleBranchIOLinkClick(String url) {
        Intent intent = ((TopChatRouter) getActivity().getApplication()).getSplashScreenIntent(getContext());
        intent.putExtra("branch", url);
        intent.putExtra("branch_force_new_session", true);
        startActivity(intent);
    }

    @Override
    public boolean isBranchIOLink(String url) {
        if (url == null) return false;
        Uri uri = Uri.parse(url);
        if (uri.getHost() != null && uri.getHost().equals(BRANCH_IO_HOST)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean needCreateWebSocket() {
        return getArguments().getBoolean(PARAM_WEBSOCKET);
    }

    @Override
    public void hideNotifier() {
        notifier.setVisibility(View.GONE);
    }

    private void initVar() {
        if (needCreateWebSocket()) {
            networkType = MODE_WEBSOCKET;
        } else {
            networkType = MODE_API;
        }
        typeFactory = new ChatRoomTypeFactoryImpl(this);
        templateChatFactory = new TemplateChatTypeFactoryImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);

        adapter = new ChatRoomAdapter(typeFactory);
        adapter.setNav(getArguments().getString(InboxMessageConstant.PARAM_NAV, ""));
        mode = getArguments().getInt(InboxMessageConstant.PARAM_MODE, InboxChatViewModel.GET_CHAT_MODE);

        templateAdapter = new TemplateChatAdapter(templateChatFactory);

        if (needCreateWebSocket()) {
            presenter.getReply(mode);
        } else {
            presenter.getExistingChat();
        }

        adapter.notifyDataSetChanged();
        templateAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
        templateRecyclerView.setAdapter(templateAdapter);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(false);
        LinearLayoutManager templateLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager
                .HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        templateRecyclerView.setLayoutManager(templateLayoutManager);
        rvQuickReply.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int index = layoutManager.findLastVisibleItemPosition();
                if (index != -1 && adapter.checkLoadMore(index)) {
                    presenter.onLoadMore();
                }
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
            }
        });

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = recyclerView.getRootView().getHeight() - recyclerView.getHeight();
                if (heightDiff > dpToPx(getActivity(), 200)) { // if more than 200 dp, it's
                    // probably a keyboard...
//                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                        .appComponent(appComponent).build();
        daggerInboxChatComponent.inject(this);
    }

    @Override
    public void setHeader() {
        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = toolbar.findViewById(R.id.user_avatar);
            onlineStatus = toolbar.findViewById(R.id.online_status);
            user = toolbar.findViewById(R.id.title);
            onlineDesc = toolbar.findViewById(R.id.subtitle);
            label = toolbar.findViewById(R.id.label);
            ImageHandler.loadImageCircle2(getActivity(), avatar, avatarImage,
                    R.drawable.ic_image_avatar_boy);

            user.setText(title);
            String senderTag = getArguments().getString(InboxMessageConstant.PARAM_SENDER_TAG, "");
            if (!TextUtils.isEmpty(getArguments().getString(InboxMessageConstant.PARAM_SENDER_TAG, ""))
                    && !senderTag.equals(InboxChatConstant.USER_TAG)) {
                label.setText(getArguments().getString(InboxMessageConstant.PARAM_SENDER_TAG));
                label.setVisibility(View.VISIBLE);
                if (senderTag.equals(InboxChatConstant.SELLER_TAG)) {
                    label.setBackgroundResource(R.drawable.topchat_seller_label);
                    label.setTextColor(getContext().getResources().getColor(R.color.medium_green));
                } else {
                    label.setBackgroundResource(R.drawable.topchat_admin_label);
                    label.setTextColor(getContext().getResources().getColor(R.color.topchat_admin_label_text_color));
                }
            } else {
                label.setVisibility(View.GONE);
            }

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProfilePage();
                }
            });
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProfilePage();
                }
            });
            setOnlineDesc(lastOnline, isOnline);
        }
    }


    private void openProfilePage() {
        String senderId = getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID);
        if (TextUtils.isEmpty(senderId)) {
            senderId = getArguments().getString(ChatRoomActivity.PARAM_USER_ID);
        }
        TrackingUtils.sendGTMEvent(
                getActivity(),
                new EventTracking(
                        "clickInboxChat",
                        "message room",
                        "click header - shop icon",
                        ""
                ).getEvent()
        );
        presenter.onGoToDetail(senderId,
                getArguments().getString(ChatRoomActivity.PARAM_SENDER_ROLE),
                getArguments().getString(ChatRoomActivity.PARAM_SOURCE, ""));
    }

    @Override
    public void displayReplyField(boolean textAreaReply) {
        if (textAreaReply) {
            replyView.setVisibility(View.VISIBLE);
        } else {
            replyView.setVisibility(View.GONE);
        }
    }

    @Override
    public ChatRoomAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setOnlineDesc(final String when, final boolean isOnline) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (onlineDesc != null && when != null)
                        onlineDesc.setText(when);
                    if (onlineStatus != null) {
                        if (isOnline)
                            onlineStatus.setImageResource(R.drawable.status_indicator_online);
                        else onlineStatus.setImageResource(R.drawable.status_indicator_offline);
                    }
                }
            });
        }
    }

    @Override
    public WebSocketInterface getInterface() {
        return this;
    }

    @Override
    public void setResult(final ChatRoomViewModel model) {
        if (getActivity() != null
                && presenter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    presenter.setResult(model);
                    presenter.initialChatSettings();
                }
            });
        }
        setResult();
    }

    @Override
    public void notifyConnectionWebSocket() {
        if (getActivity() != null && presenter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifier.setVisibility(View.VISIBLE);
                    TextView title = notifier.findViewById(R.id.title);
                    View action = notifier.findViewById(R.id.action);
                    title.setText(R.string.error_no_connection_retrying);
                    action.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void showError(String error) {
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new
                    NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            mode = getArguments().getInt(InboxMessageConstant.PARAM_MODE, InboxChatViewModel
                                    .GET_CHAT_MODE);
                            presenter.getReply(mode);
                        }
                    });
        } else {
            if (error.equals("")) {
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                NetworkErrorHelper.showSnackbar(getActivity(), error);
            }
        }
    }

    @Override
    public void setUploadingMode(boolean mode) {
        uploading = mode;
    }

    @Override
    public void scrollToBottom() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void scrollToBottomWithCheck() {
        int index = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (index < 3) {
            scrollToBottom();
        }
    }

    @Override
    public void setHeaderModel(String nameHeader, String imageHeader) {
        this.avatarImage = imageHeader;
        this.title = nameHeader;
    }

    @Override
    public void resetReplyColumn() {
        replyColumn.setText("");
    }

    @Override
    public void setTemplate(List<Visitable> listTemplate) {
        if (listTemplate == null) {
            templateRecyclerView.setVisibility(View.GONE);
        } else {
            templateRecyclerView.setVisibility(View.VISIBLE);
            templateAdapter.setList(listTemplate);
        }
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {

    }

    @Override
    public void disableAction() {
        sendButton.setEnabled(false);
    }

    private void setResult() {
        if (adapter != null && getActivity() != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if (adapter.getLastItem() != null)
                bundle.putParcelable(PARCEL, adapter.getLastItem());
            intent.putExtras(bundle);
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
    }

    @Override
    public void setCanLoadMore(boolean hasNext) {
        if (hasNext) {
            adapter.showLoading();
        } else {
            adapter.removeLoading();
        }
    }

    public void restackList(Bundle data) {

        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = toolbar.findViewById(R.id.user_avatar);
            user = toolbar.findViewById(R.id.title);
            onlineDesc = toolbar.findViewById(R.id.subtitle);
            label = toolbar.findViewById(R.id.label);
            ImageHandler.loadImageCircle2(getActivity(), avatar, null, R.drawable
                    .ic_image_avatar_boy);
            title = getArguments().getString(InboxMessageConstant.PARAM_SENDER_NAME);
            user.setText(title);
            label.setText(getArguments().getString(InboxMessageConstant.PARAM_SENDER_TAG));
            setOnlineDesc(this.lastOnline, this.isOnline);
        }
    }

    @Override
    public void hideMainLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            presenter.stopTyping(getArguments().getString(ChatRoomActivity
                    .PARAM_MESSAGE_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        presenter.closeWebSocket();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    private View.OnClickListener getSendInitMessage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventSendMessagePage(getActivity());
                presenter.initMessage(replyColumn.getText().toString(),
                        getArguments().getString(ChatRoomActivity.PARAM_SOURCE),
                        getArguments().getString(ChatRoomActivity.PARAM_SENDER_ID),
                        getArguments().getString(ChatRoomActivity.PARAM_USER_ID));
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    if (!isChatBot) presenter.getTemplate();
                    break;
                }
                break;
            case ImageUploadHandlerChat.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String fileLoc = presenter.getFileLocFromCamera();
                    ImageUploadViewModel temp = generateChatViewModelWithImage(fileLoc);
                    presenter.startUpload(Collections.singletonList(temp), networkType);
                    adapter.addReply(temp);
                }
                break;
            case REQUEST_CODE_CHAT_IMAGE:
                if (resultCode != Activity.RESULT_OK || data == null) {
                    return;
                }
                ArrayList<String> imagePathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS);
                if (imagePathList == null || imagePathList.size() <= 0) {
                    return;
                }
                List<ImageUploadViewModel> list = new ArrayList<>();
                String imagePath = imagePathList.get(0);
                if (!TextUtils.isEmpty(imagePath)) {
                    ImageUploadViewModel temp = generateChatViewModelWithImage(imagePath);
                    list.add(temp);
                } else {
                    for (int i = 0; i < imagePathList.size(); i++) {
                        ImageUploadViewModel temp = generateChatViewModelWithImage(imagePathList.get(i));
                        list.add(temp);
                    }
                }
                adapter.addReply(list);
                presenter.startUpload(list, networkType);
                break;
            case AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE:
                if (data == null)
                    break;
                if (!data.hasExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY))
                    break;
                ArrayList<ResultProduct> resultProducts = data.getParcelableArrayListExtra
                        (AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY);
                attachProductRetrieved(resultProducts);
                break;
            case AttachInvoiceActivity.TOKOPEDIA_ATTACH_INVOICE_REQ_CODE:
                if (data == null)
                    break;
                if (!data.hasExtra(AttachInvoiceActivity
                        .TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY))
                    break;
                SelectedInvoice selectedInvoice = data.getParcelableExtra(AttachInvoiceActivity
                        .TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY);
                attachInvoiceRetrieved(AttachInvoiceMapper.convertInvoiceToDomainInvoiceModel(selectedInvoice));
                break;
            case ChatRoomFragment.CHAT_GO_TO_SHOP_DETAILS_REQUEST:
                presenter.getFollowStatus(getArguments().getString(ChatRoomActivity
                        .PARAM_SENDER_ID, ""));
            case REQUEST_CODE_CHAT_SETTINGS:
                if (resultCode == RESULT_CODE_CHAT_SETTINGS_ENABLED) {
                    sendMessageLayout.setVisibility(View.VISIBLE);
                    templateRecyclerView.setVisibility(View.VISIBLE);
                    chatBlockLayout.setVisibility(View.GONE);
                } else if (resultCode == RESULT_CODE_CHAT_SETTINGS_DISABLED) {
                    sendMessageLayout.setVisibility(View.GONE);
                    templateRecyclerView.setVisibility(View.GONE);
                    chatBlockLayout.setVisibility(View.VISIBLE);
                    setBlockedLayout();
                }
                presenter.initialChatSettings();
                break;
            default:
                break;
        }
    }

    public void onBackPressed() {
        if (uploading) {
            showDialogConfirmToAbortUpload();
        } else {
            ((ChatRoomActivity) getActivity()).destroy();
        }
    }

    private void showDialogConfirmToAbortUpload() {
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle(getActivity().getString(R.string.exit_chat_title));
        myAlertDialog.setMessage(getActivity().getString(R.string.exit_chat_body));
        myAlertDialog.setPositiveButton(getActivity().getString(R.string.exit_chat_yes), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ChatRoomActivity) getActivity()).destroy();
                    }
                });
        myAlertDialog.setNegativeButton(getActivity().getString(R.string.cancel), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public boolean shouldHandleUrlManually(String url) {
        String urlManualHandlingList[] = {CONTACT_US_URL_BASE_DOMAIN};
        return (Arrays.asList(urlManualHandlingList).contains(url) || isChatBot);
    }

    @Override
    public void showSnackbarError(String string) {
        NetworkErrorHelper.showSnackbar(getActivity(), string);
    }

    //Getter
    @Override
    public String getKeyword() {
        return getArguments().getString(InboxMessageConstant.PARAM_KEYWORD);
    }

    @Override
    public String getReplyMessage() {
        return replyColumn.getText().toString();
    }

    @Override
    public UserSession getUserSession() {
        return ((AbstractionRouter) getActivity().getApplication()).getSession();
    }

    //Clicker (from viewholder) with it's tracker
    @Override
    public void productClicked(Integer productId, String productName, String productPrice, Long
            dateTimeReply, String url) {
        trackProductClicked();
        String senderRole = getArguments().getString(ChatRoomActivity.PARAM_SENDER_ROLE, "");
        if (!GlobalConfig.isSellerApp() || !senderRole.equals(ROLE_SHOP)) {
            if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
                TkpdInboxRouter router = (TkpdInboxRouter) MainApplication.getAppContext();
                ProductPass productPass = ProductPass.Builder.aProductPass()
                        .setProductId(productId)
                        .setProductPrice(productPrice)
                        .setProductName(productName)
                        .setDateTimeInMilis(dateTimeReply)
                        .build();

                Intent intent = router.getProductDetailIntent(getContext(), productPass);
                startActivity(intent);
            }
        } else {
            //Necessary to do it this way to prevent PDP opened in seller app
            //otherwise someone other than the owner can access PDP with topads promote page
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }


    private void trackProductClicked() {
        if ((getActivity().getApplicationContext() instanceof AbstractionRouter)) {
            AbstractionRouter abstractionRouter = (AbstractionRouter) getActivity()
                    .getApplicationContext();
            abstractionRouter.getAnalyticTracker().sendEventTracking(
                    AttachProductAnalytics.getEventClickChatAttachedProductImage().getEvent()
            );
        }
    }

    @Override
    public void onInvoiceSelected(InvoiceLinkPojo selectedInvoice) {
        attachInvoiceRetrieved(selectedInvoice);
    }

    @Override
    public void showSearchInvoiceScreen() {
        String msgId = getArguments().getString(InboxMessageConstant.PARAM_MESSAGE_ID);
        String userId = SessionHandler.getLoginID(getContext());
        Intent intent = AttachInvoiceActivity.createInstance(getActivity(), userId
                , Integer.parseInt(msgId));
        startActivityForResult(intent, AttachInvoiceActivity.TOKOPEDIA_ATTACH_INVOICE_REQ_CODE);
    }

    @Override
    public void onClickRating(ChatRatingViewModel element, int rating) {
        UserSession userSession = ((AbstractionRouter) getContext().
                getApplicationContext()).getSession();
        int userId = 0;
        if (userSession != null && !TextUtils.isEmpty(userSession.getUserId())) {
            userId = Integer.valueOf(userSession.getUserId());
        }
        presenter.setChatRating(element, userId, rating);
    }

    @Override
    public void onGoToTimeMachine(String url) {
        startActivity(TimeMachineActivity.getCallingIntent(getActivity(), url));
    }


    //CHECK CONDITION

    @Override
    public boolean isChatBot() {
        return isChatBot;
    }

    @Override
    public boolean isMyMessage(int fromUid) {
        return String.valueOf(fromUid).equals(SessionHandler.getLoginID(MainApplication
                .getAppContext()));
    }

    @Override
    public boolean isMyMessage(String fromUid) {
        return fromUid.equals(SessionHandler.getLoginID(MainApplication
                .getAppContext()));
    }

    @Deprecated
    @Override
    public boolean isCurrentThread(int msgId) {
        return getArguments().getString(InboxMessageConstant.PARAM_MESSAGE_ID, "").equals(String.valueOf(msgId));
    }

    public boolean isCurrentThread(String msgId) {
        return getArguments().getString(InboxMessageConstant.PARAM_MESSAGE_ID, "").equals(msgId);
    }

    public boolean isAllowedTemplate() {
        return true;
    }


    //ADD INCOMING MESSAGE

    private AttachInvoiceSentViewModel generateInvoice(InvoiceLinkPojo selectedInvoice) {
        InvoiceLinkAttributePojo invoiceLinkAttributePojo = selectedInvoice.getAttributes();
        return new AttachInvoiceSentViewModel(
                getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID),
                sessionHandler.getLoginName(),
                invoiceLinkAttributePojo.getTitle(),
                invoiceLinkAttributePojo.getDescription(),
                invoiceLinkAttributePojo.getImageUrl(),
                invoiceLinkAttributePojo.getTotalAmount(),
                SendableViewModel.generateStartTime()
        );
    }

    public ImageUploadViewModel generateChatViewModelWithImage(String imageUrl) {
        scrollToBottom();

        ImageUploadViewModel model = new ImageUploadViewModel(
                getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID),
                String.valueOf(System.currentTimeMillis() / MILIS_TO_SECOND),
                imageUrl,
                SendableViewModel.generateStartTime()
        );
        return model;
    }

    private ProductAttachmentViewModel generateProductChatViewModel(ResultProduct product) {
        return new ProductAttachmentViewModel(
                sessionHandler.getLoginID(),
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getProductUrl(),
                product.getProductImageThumbnail(),
                SendableViewModel.generateStartTime());
    }

    private void attachInvoiceRetrieved(InvoiceLinkPojo selectedInvoice) {
        String msgId = getArguments().getString(PARAM_MESSAGE_ID);
        AttachInvoiceSentViewModel generatedInvoice = generateInvoice(selectedInvoice);
        adapter.addReply(generatedInvoice);
        presenter.sendInvoiceAttachment(msgId, selectedInvoice, generatedInvoice.getStartTime());
        scrollToBottom();
    }

    public void attachProductRetrieved(ArrayList<ResultProduct> resultProducts) {
        UnifyTracking.eventSendAttachment(getActivity(), TopChatAnalytics.Category.CHAT_DETAIL,
                TopChatAnalytics.Action.CHAT_DETAIL_ATTACHMENT,
                TopChatAnalytics.Name.CHAT_DETAIL);

        String msgId = getArguments().getString(InboxMessageConstant.PARAM_MESSAGE_ID);
        for (ResultProduct result : resultProducts) {
            ProductAttachmentViewModel item = generateProductChatViewModel(result);
            presenter.sendProductAttachment(msgId, result, item.getStartTime());
            adapter.addReply(item);
            scrollToBottom();
        }
    }

    private void addViewMessage(ReplyActionData replyData, String reply) {
        setViewEnabled(true);
        MessageViewModel messageViewModel = new MessageViewModel(
                String.valueOf(replyData.getChat().getMsgId()),
                replyData.getChat().getSenderId(),
                replyData.getChat().getFrom(),
                "",
                "",
                "",
                replyData.getChat().getReplyTime(),
                "",
                reply,
                false,
                false,
                true
        );

        adapter.addReply(messageViewModel);
        resetReplyColumn();
        setResult();
    }

    @Override
    public void addTimeMachine() {
        adapter.showTimeMachine();
    }

    @Override
    public void addDummyMessage(String dummyText, String startTime) {

        MessageViewModel dummyChat = new MessageViewModel(
                getArguments().getString(InboxMessageConstant.PARAM_MESSAGE_ID),
                sessionHandler.getLoginID(),
                sessionHandler.getLoginName(),
                startTime,
                dummyText.length() != 0 ? dummyText : getReplyMessage()
        );
        adapter.addReply(dummyChat);
        scrollToBottom();
    }

    //Callback
    @Override
    public void onOpenWebSocket() {
        if (getActivity() != null && presenter != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView title = notifier.findViewById(R.id.title);
                    title.setText(R.string.connected_websocket);
                    View action = notifier.findViewById(R.id.action);
                    action.setVisibility(View.GONE);
                    networkType = MODE_WEBSOCKET;
                    sendButton.setOnClickListener(generateSendClickListener());

                }
            });
            notifier.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifier.setVisibility(View.GONE);
                }
            }, 1500);
            presenter.onOpenWebSocket();
        }
    }

    @Override
    public void onErrorWebSocket() {
        if (getActivity() != null && presenter != null) {
            networkType = MODE_API;
            sendButton.setOnClickListener(generateSendClickListener());
            notifyConnectionWebSocket();
            presenter.recreateWebSocket();
        }
    }

    @Override
    public void onIncomingEvent(WebSocketResponse response) {
        switch (response.getCode()) {
            case ChatWebSocketConstant.EVENT_TOPCHAT_TYPING:
                if (String.valueOf(response.getData().getMsgId()).equals(getArguments().getString
                        (InboxMessageConstant.PARAM_MESSAGE_ID))) {
                    setOnlineDesc(getString(R.string.is_typing), this.isOnline);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.showTyping();
                            if (layoutManager.findFirstCompletelyVisibleItemPosition() < 2) {
                                scrollToBottom();
                            }
                        }
                    });
                }
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING:
                if (String.valueOf(response.getData().getMsgId()).equals(getArguments().getString
                        (InboxMessageConstant.PARAM_MESSAGE_ID))) {
                    setOnlineDesc(this.lastOnline, this.isOnline);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter.isTyping()) {
                                adapter.removeTyping();
                                if (layoutManager.findFirstCompletelyVisibleItemPosition() < 2) {
                                    scrollToBottom();
                                }
                            }
                        }
                    });
                }
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_READ_MESSAGE:
                adapter.setReadStatus();
                break;
            default:
                break;
        }
    }

    @Override
    public void onReceiveMessage(final BaseChatViewModel message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isCurrentThread(message.getMessageId())) {
                        processReceiveMessage(message);
                    }
                }
            });
        }
    }

    private void processReceiveMessage(BaseChatViewModel message) {
        if (templateAdapter != null && templateAdapter.getList().size() != 0) {
            templateRecyclerView.setVisibility(View.VISIBLE);
        }

        setViewEnabled(true);
        removeDummyReplyIfExist(message);
        removeIsTyping();

        mapMessageToList(message);

        if (isMyMessage(message.getFromUid())) {
            scrollToBottom();
            resetReplyColumn();
        } else {
            scrollToBottomWithCheck();
            readMessage(message.getMessageId());
        }

        checkHideQuickReply(message);
        setResult();
    }

    private void mapMessageToList(BaseChatViewModel message) {
        if (message instanceof QuickReplyListViewModel) {
            showQuickReplyView((QuickReplyListViewModel) message);
            if (!TextUtils.isEmpty(message.getMessage())) {
                addMessageToList(message);
            }
        } else {
            addMessageToList(message);
        }
    }

    private void checkHideQuickReply(BaseChatViewModel message) {
        if (TextUtils.isEmpty(message.getAttachmentId())
                && quickReplyAdapter != null
                && rvQuickReply != null
                && !isMyMessage(message.getFromUid())) {
            quickReplyAdapter.clearData();
            rvQuickReply.setVisibility(View.GONE);
        }
    }

    private void removeDummyReplyIfExist(BaseChatViewModel message) {
        if (isMyMessage(message.getFromUid())) {
            if (message instanceof SendableViewModel) {
                getAdapter().removeLastMessageWithStartTime(((SendableViewModel) message).getStartTime());
            } else {
                getAdapter().removeLast();
            }
        }
    }

    private void readMessage(String messageId) {
        if (!TextUtils.isEmpty(messageId)) {
            presenter.readMessage(messageId);
        }
    }

    private void addMessageToList(BaseChatViewModel message) {
        adapter.addReply((Visitable) message);
        setResult();
    }

    private void removeIsTyping() {
        if (adapter.isTyping()) {
            adapter.removeTyping();
        }
    }

    @Override
    public void showQuickReplyView(QuickReplyListViewModel model) {
        if (model.getQuickReplies().size() != 0) {
            rvQuickReply.setVisibility(View.VISIBLE);
            templateRecyclerView.setVisibility(View.GONE);
            quickReplyAdapter = new QuickReplyAdapter(model, this);
            rvQuickReply.setAdapter(quickReplyAdapter);
            rvQuickReply.getAdapter().notifyDataSetChanged();
        } else if (quickReplyAdapter != null) {
            quickReplyAdapter.clearData();
            rvQuickReply.setVisibility(View.GONE);
        }
    }

    @Override
    public void onQuickReplyClicked(QuickReplyListViewModel quickReplyListViewModel, QuickReplyViewModel quickReply) {
        if (getArguments() != null) {
            if (templateAdapter != null && templateAdapter.getList().size() != 0) {
                templateRecyclerView.setVisibility(View.VISIBLE);
            }
            String msgId = getArguments().getString(PARAM_MESSAGE_ID, "");

            presenter.sendQuickReply(msgId, quickReply, SendableViewModel.generateStartTime());
        }
    }

    @Override
    public void onChatActionBalloonSelected(ChatActionBubbleViewModel message, Visitable modelToBeRemoved) {
        presenter.sendMessage(networkType, message.getMessage());
        adapter.remove(modelToBeRemoved);
    }

    @Override
    public void onSuccessSendReply(ReplyActionData replyData, String reply) {
        adapter.removeLast();
        addViewMessage(replyData, reply);
        if (quickReplyAdapter.getItemCount() != 0) {
            quickReplyAdapter.clearData();
        }
    }

    @Override
    public void onErrorSendReply() {
        adapter.removeLast();
        setViewEnabled(true);
        replyColumn.setText("");
        showError(getActivity().getString(R.string.delete_error).concat("\n").concat(getString(R
                .string.string_general_error)));
        if (quickReplyAdapter != null && quickReplyAdapter.getItemCount() != 0) {
            rvQuickReply.setVisibility(View.VISIBLE);
            templateRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessSendAttach(ReplyActionData data, ImageUploadViewModel model) {
        adapter.remove(model);
        addViewImage(data, UPLOADING);
    }

    private void addViewImage(ReplyActionData replyData, String message) {
        setViewEnabled(true);
        ImageUploadViewModel messageViewModel = new ImageUploadViewModel(
                String.valueOf(replyData.getChat().getMsgId()),
                replyData.getChat().getSenderId(),
                replyData.getChat().getFrom(),
                "",
                replyData.getChat().getAttachment().getId(),
                replyData.getChat().getAttachment().getType(),
                replyData.getChat().getReplyTime(),
                true,
                replyData.getChat().getAttachment().getAttributes().getImageUrl(),
                replyData.getChat().getAttachment().getAttributes().getThumbnail(),
                "",
                message
        );

        adapter.addReply(messageViewModel);
        resetReplyColumn();
        setResult();
    }

    @Override
    public void onSuccessSetRating(ChatRatingViewModel model) {
        adapter.changeRating(model);
    }

    @Override
    public void onErrorSetRating(String errorMessage) {
        showError(errorMessage);
    }

    @Override
    public void onErrorUploadImages(String errorMessage, ImageUploadViewModel model) {
        showError(errorMessage);
        showRetryFor(model);
    }

    @Override
    public void onSuccessInitMessage() {
        CommonUtils.UniversalToast(getActivity(), getString(R.string.success_send_msg));
        getActivity().finish();
    }

    @Override
    public void onErrorInitMessage(String s) {
        adapter.removeLast();
        NetworkErrorHelper.showSnackbar(getActivity(), s);
        sendButton.setEnabled(true);
    }

    @Override
    public void setMessageId(String messageId) {
        if (getArguments() != null) {
            getArguments().putString(ChatRoomActivity.PARAM_MESSAGE_ID, messageId);
        }
    }

    @Override
    public void enableWebSocket() {
        if (getArguments() != null) {
            getArguments().putBoolean(ChatRoomActivity.PARAM_WEBSOCKET, true);
        }
        setPickerButton();
    }

    @Override
    public void showReasonRating(String messageId, long replyTimeNano, ArrayList<String> reasons) {

        if (reasonBottomSheet == null) {
            reasonBottomSheet = ReasonBottomSheet.createInstance(getActivity(),
                    reasons, new ReasonAdapter.OnReasonClickListener() {
                        @Override
                        public void onClickReason(int adapterPosition) {
                            presenter.sendReasonRating(messageId,
                                    replyTimeNano,
                                    String.valueOf(reasons.get(adapterPosition)));
                            reasonBottomSheet.dismiss();
                        }
                    });
        }
        reasonBottomSheet.show();
    }

    @Override
    public void setUserStatus(String status, boolean isOnline) {
        this.lastOnline = status;
        this.isOnline = isOnline;
        setOnlineDesc(status, isOnline);
    }

    public void showBottomSheetMenu() {
        boolean isFavorited = getArguments().getBoolean(IS_FAVORITE_KEY, false);
        boolean isShop = getArguments().getBoolean(IS_SHOP_KEY, false);

        Menus headerMenu = new Menus(getContext());
        List<Menus.ItemMenus> listMenu = new ArrayList<>();
        String profileText = getString(R.string.follow_store);
        if (isFavorited) profileText = getString(R.string.already_follow_store);

        if (isShop) listMenu.add(new Menus.ItemMenus(profileText, R.drawable.ic_chat_add_grey));
        listMenu.add(new Menus.ItemMenus(getString(R.string.delete_conversation), R.drawable.ic_trash));
        if (showChatSettingMenu) {
            listMenu.add(new Menus.ItemMenus(getString(R.string.chat_incoming_settings), R.drawable.ic_chat_settings));
        }

        headerMenu.setItemMenuList(listMenu);
        headerMenu.setActionText(getString(R.string.cancel_bottom_sheet));
        headerMenu.setOnActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerMenu.dismiss();
            }
        });
        headerMenu.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
            @Override
            public void onClick(Menus.ItemMenus itemMenus, int pos) {
                if (itemMenus.title.equalsIgnoreCase(getString(R.string.delete_conversation))) {
                    showDeleteChatDialog();
                } else if (pos == 0) {
                    String senderId = getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID);
                    if (TextUtils.isEmpty(senderId)) {
                        senderId = getArguments().getString(ChatRoomActivity.PARAM_USER_ID);
                    }
                    TrackingUtils.sendGTMEvent(
                            getActivity(),
                            new EventTracking(
                                    "clickInboxChat",
                                    "message room",
                                    "click header - three bullet",
                                    "lihat profile"
                            ).getEvent()
                    );
                    presenter.onGoToDetail(senderId,
                            getArguments().getString(ChatRoomActivity.PARAM_SENDER_ROLE),
                            getArguments().getString(ChatRoomActivity.PARAM_SOURCE, ""));
                } else if (itemMenus.title.equalsIgnoreCase(getString(R.string.follow_store))) {
                    TrackingUtils.sendGTMEvent(
                            getActivity(),
                            new EventTracking(
                                    "clickInboxChat",
                                    "message room",
                                    "click header - three bullet",
                                    "follow shop"
                            ).getEvent()
                    );
                    presenter.doFollowUnfollowToggle(getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID));
                } else if (itemMenus.title.equalsIgnoreCase(getString(R.string.already_follow_store))) {
                    TrackingUtils.sendGTMEvent(
                            getActivity(),
                            new EventTracking(
                                    "clickInboxChat",
                                    "message room",
                                    "click header - three bullet",
                                    "unfollow shop"
                            ).getEvent()
                    );
                    presenter.doFollowUnfollowToggle(getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID));
                } else if (itemMenus.title.equalsIgnoreCase(getString(R.string.chat_incoming_settings))) {
                    Intent intent = ChatRoomSettingsActivity.getIntent(getContext(),
                            getArguments().getString(ChatRoomActivity.PARAM_MESSAGE_ID),
                            chatSettingsResponse,
                            isChatEnabled, role, senderName);
                    chatSettingsAnalytics.sendOpenChatSettingTacking();
                    startActivityForResult(intent, REQUEST_CODE_CHAT_SETTINGS);
                }
                headerMenu.dismiss();
            }
        });
        headerMenu.show();
    }

    @Override
    public void successDeleteChat() {
        String messageId = getArguments().getString(ChatRoomActivity.PARAM_MESSAGE_ID, "");
        Intent data = new Intent();
        data.putExtra(ChatRoomActivity.PARAM_MESSAGE_ID, messageId);
        getActivity().setResult(CHAT_DELETED_RESULT_CODE, data);
        getActivity().finish();
    }

    private void showDeleteChatDialog() {
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle(R.string.delete_chat_question);
        myAlertDialog.setMessage(R.string.delete_chat_warning_message);
        myAlertDialog.setPositiveButton(getString(R.string.delete), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.deleteChat(getArguments().getString(MESSAGE_ID));
                    }
                });
        myAlertDialog.setNegativeButton(getActivity().getString(R.string.cancel), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void setChatShopInfoData(ChatShopInfoViewModel viewModel) {
        getArguments().putBoolean(IS_FAVORITE_KEY, viewModel.isFavorited());
        getArguments().putBoolean(IS_SHOP_KEY, viewModel.isShop());
    }

    @Override
    public void toggleFollowSuccess() {
        boolean isFollow = getArguments().getBoolean(IS_FAVORITE_KEY, false);
        getArguments().putBoolean(IS_FAVORITE_KEY, !isFollow);
    }

    @Override
    public void setMenuVisible(boolean isVisible) {
        if (headerMenuButton != null) {
            if (isVisible) headerMenuButton.setVisibility(View.VISIBLE);
            else headerMenuButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void setInboxMessageVisibility(ChatSettingsResponse chatSettingsResponse, boolean isVisible) {
        this.chatSettingsResponse = chatSettingsResponse;
        if (isVisible) {
            sendMessageLayout.setVisibility(View.GONE);
            templateRecyclerView.setVisibility(View.GONE);
            chatBlockLayout.setVisibility(View.VISIBLE);
            setBlockedLayout();
        } else {
            sendMessageLayout.setVisibility(View.VISIBLE);
            templateRecyclerView.setVisibility(View.VISIBLE);
            chatBlockLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void shouldShowChatSettingsMenu(boolean showChatSettingMenu) {
        this.showChatSettingMenu = showChatSettingMenu;
    }

    private void setBlockedLayout() {
        if (chatSettingsResponse != null && chatSettingsResponse.getChatBlockResponse() != null) {
            String category = "";
            if (role.equalsIgnoreCase(InboxChatConstant.OFFICIAL_TAG)) {
                category = InboxChatConstant.CHAT_PROMOTION;
               } else if (role.equalsIgnoreCase(InboxChatConstant.SELLER_TAG)) {
                category = InboxChatConstant.CHAT_BOTH;
            } else if (role.equalsIgnoreCase(InboxChatConstant.USER_TAG)) {
                category = InboxChatConstant.CHAT_PERSONAL;
            }
            blockedText.setText(String.format(getResources().getString(R.string.chat_blocked_text), category, senderName, Utils.getDateTime(chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().getValidDate())));
        }
    }


    private void disableChatSettingst() {
        String category = "";
        if (chatSettingsResponse != null && chatSettingsResponse.getChatBlockResponse() != null) {
            if (role.equalsIgnoreCase(InboxChatConstant.OFFICIAL_TAG)) {
                category = InboxChatConstant.CHAT_PROMOTION;
            } else if (role.equalsIgnoreCase(InboxChatConstant.SELLER_TAG)) {
                category = InboxChatConstant.CHAT_BOTH;
            } else if (role.equalsIgnoreCase(InboxChatConstant.USER_TAG)) {
                category = InboxChatConstant.CHAT_PERSONAL;
            }
            ToasterNormal.show(getActivity(), String.format(getResources().getString(R.string.enable_chat_toast), category, senderName));
        }
    }
}
