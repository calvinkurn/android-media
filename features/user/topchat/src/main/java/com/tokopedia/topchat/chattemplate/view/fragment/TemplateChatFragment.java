package com.tokopedia.topchat.chattemplate.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics;
import com.tokopedia.topchat.chattemplate.di.DaggerTemplateChatComponent;
import com.tokopedia.topchat.chattemplate.di.TemplateChatModule;
import com.tokopedia.topchat.chattemplate.view.activity.EditTemplateChatActivity;
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatSettingAdapter;
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatSettingTypeFactoryImpl;
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder;
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract;
import com.tokopedia.topchat.chattemplate.view.presenter.TemplateChatSettingPresenter;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.util.SimpleItemTouchHelperCallback;
import com.tokopedia.unifycomponents.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity.PARAM_IS_SELLER;

public class TemplateChatFragment extends BaseDaggerFragment
        implements TemplateChatContract.View {


    public static final int CREATE = 0;
    public static final int EDIT = 1;
    public static final int DELETE = -1;

    public static final String LIST_RESULT = "string";
    public static final String INDEX_RESULT = "index";
    public static final String MODE_RESULT = "mode";

    private SwitchCompat switchTemplate;
    private RecyclerView recyclerView;
    private View templateContainer;
    private View info;
    private View loading;
    private View content;
    private TemplateChatSettingTypeFactoryImpl typeFactory;
    private TemplateChatSettingAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Inject
    TemplateChatSettingPresenter presenter;

    @Inject
    ChatTemplateAnalytics analytic;

    private ItemTouchHelper mItemTouchHelper;
    private BottomSheetView bottomSheetView;
    private Boolean isSeller = false;

    public static TemplateChatFragment createInstance(Bundle extras) {
        TemplateChatFragment fragment = new TemplateChatFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();
    }

    private void initArguments() {
        if (getArguments() != null) {
            isSeller = getArguments().getBoolean(PARAM_IS_SELLER);
        }
        if (GlobalConfig.isSellerApp()) {
            isSeller = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_template_chat, container, false);
        typeFactory = new TemplateChatSettingTypeFactoryImpl(this);

        loading = rootView.findViewById(R.id.loading_search);
        content = rootView.findViewById(R.id.content);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        info = rootView.findViewById(R.id.template_list_info);
        switchTemplate = rootView.findViewById(R.id.switch_chat_template);
        templateContainer = rootView.findViewById(R.id.template_container);

        recyclerView.setHasFixedSize(true);

        presenter.attachView(this);
        presenter.setMode(isSeller);
        presenter.getTemplate();
        setBottomSheetDialog();
        return rootView;
    }

    private void setBottomSheetDialog() {
        bottomSheetView = new BottomSheetView(getActivity());
        bottomSheetView.setTitleTextSize(getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl3));
        bottomSheetView.setBodyTextSize(getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl3));
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getActivity().getString(R.string.title_info_list_template))
                .setBody(getActivity().getString(R.string.body_info_list_template))
                .setImg(R.drawable.drag_edit)
                .build());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoading();
        adapter = new TemplateChatSettingAdapter(typeFactory, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        switchTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = switchTemplate.isChecked();
                analytic.trackOnCheckedChange(b);
                presenter.switchTemplateAvailability(b);
                if (b) {
                    templateContainer.setVisibility(View.VISIBLE);
                } else {
                    templateContainer.setVisibility(View.GONE);
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetView.show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

        if (getActivity() != null && getActivity().getApplication() != null) {
            BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication())
                    .getBaseAppComponent();
            DaggerTemplateChatComponent daggerTemplateChatComponent =
                    (DaggerTemplateChatComponent) DaggerTemplateChatComponent.builder()
                            .baseAppComponent(appComponent)
                            .templateChatModule(new TemplateChatModule(getContext()))
                            .build();
            daggerTemplateChatComponent.inject(this);
        }
    }

    @Override
    public void setTemplate(List<Visitable> listTemplate) {
        adapter.setList(listTemplate);
        if (listTemplate != null) prepareResult();
    }

    @Override
    public void onDrag(ItemTemplateChatViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onEnter(String message, int position) {
        if (message == null && adapter.getList().size() > 5) {
            showUnifyToaster(getActivity().getString(R.string.limited_template_chat_warning), Toaster.TYPE_NORMAL);
        } else {
            Intent intent = EditTemplateChatActivity.createInstance(getActivity());
            Bundle bundle = new Bundle();
            bundle.putString(InboxMessageConstant.PARAM_MESSAGE, message);
            bundle.putInt(InboxMessageConstant.PARAM_POSITION, position);
            bundle.putInt(InboxMessageConstant.PARAM_NAV, adapter.getList().size() - 1);
            bundle.putStringArrayList(InboxMessageConstant.PARAM_ALL, adapter.getListString());
            if (message == null) {
                bundle.putInt(InboxMessageConstant.PARAM_MODE, CREATE);
                analytic.trackAddTemplateChat();
            } else {
                bundle.putInt(InboxMessageConstant.PARAM_MODE, EDIT);
                analytic.trackEditTemplateChat();
            }
            bundle.putBoolean(PARAM_IS_SELLER, isSeller);
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
            getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    @Override
    public void setChecked(boolean enable) {
        switchTemplate.setChecked(enable);
        if (enable) {
            templateContainer.setVisibility(View.VISIBLE);
        } else {
            templateContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void reArrange(int from, int to) {
        presenter.setArrange(switchTemplate.isChecked(), arrangeList(from, to), from, to);
    }

    @Override
    public void revertArrange(int from, int to) {
        adapter.revertArrange(to, from);
    }

    public ArrayList<Integer> arrangeList(int from, int to) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < adapter.getList().size() - 1; i++) {
            arrayList.add(i + 1);
        }

        arrayList.remove(Integer.valueOf(from + 1));
        arrayList.add(to, from + 1);
        return arrayList;
    }

    @Override
    public ArrayList<String> getList() {
        return adapter.getListString();
    }

    @Override
    public TemplateChatSettingAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void successSwitch() {
        prepareResultSwitch();
    }

    @Override
    public void showLoading() {
        content.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishLoading() {
        content.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable errorMessage) {
        showUnifyToaster(ErrorHandler.getErrorMessage(getContext(), errorMessage), Toaster.TYPE_ERROR);
    }

    @Override
    public void successRearrange() {
        String text = getActivity().getString(R.string.success_rearrange_template_chat);
        showUnifyToaster(text, Toaster.TYPE_NORMAL);
        prepareResult();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.reloadTemplate();
                    String string = data.getStringExtra(LIST_RESULT);
                    int index = data.getIntExtra(INDEX_RESULT, -1);
                    String text = "";
                    switch (data.getIntExtra(MODE_RESULT, 0)) {
                        case CREATE:
                            adapter.add(string);
                            text = getActivity().getString(R.string.success_add_template_chat);
                            break;
                        case EDIT:
                            adapter.edit(index, string);
                            text = getActivity().getString(R.string.success_edit_template_chat);
                            break;
                        case DELETE:
                            adapter.delete(index);
                            text = getActivity().getString(R.string.success_delete_template_chat);
                            break;
                        default:
                            break;
                    }
                    prepareResult();
                    showUnifyToaster(text, Toaster.TYPE_NORMAL);
                    break;
                }
            default:
                break;
        }
    }

    private void prepareResultSwitch() {
        if (switchTemplate.isChecked()) {
            prepareResult();
        } else {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(LIST_RESULT, new ArrayList<String>());
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
    }

    private void prepareResult() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(LIST_RESULT, adapter.getListString());
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private void showUnifyToaster(String text, int type) {
        if(getView() != null) {
            Toaster.build(getView(), text, Toaster.LENGTH_SHORT, type).show();
        }
    }
}
