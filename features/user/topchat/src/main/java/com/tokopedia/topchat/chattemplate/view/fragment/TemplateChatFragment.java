package com.tokopedia.topchat.chattemplate.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.di.DaggerTemplateChatComponent;
import com.tokopedia.topchat.chattemplate.view.activity.EditTemplateChatActivity;
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatSettingAdapter;
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatSettingTypeFactoryImpl;
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder;
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract;
import com.tokopedia.topchat.chattemplate.view.presenter.TemplateChatSettingPresenter;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.util.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    private ItemTouchHelper mItemTouchHelper;
    private Snackbar snackbarError;

    private Snackbar snackbarInfo;
    private BottomSheetView bottomSheetView;

    public static TemplateChatFragment createInstance(Bundle extras) {
        TemplateChatFragment fragment = new TemplateChatFragment();
        fragment.setArguments(extras);
        return fragment;
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
        snackbarError = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_LONG);
        snackbarInfo = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_LONG);

        recyclerView.setHasFixedSize(true);

        presenter.attachView(this);

        setBottomSheetDialog();
        return rootView;
    }

    private void setBottomSheetDialog() {
        bottomSheetView = new BottomSheetView(getActivity());
        bottomSheetView.setTitleTextSize(getResources().getDimension(R.dimen.sp_14));
        bottomSheetView.setBodyTextSize(getResources().getDimension(R.dimen.sp_14));
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

        TextView textView = snackbarError.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        TextView textView2 = snackbarInfo.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        switchTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = switchTemplate.isChecked();
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
                            .baseAppComponent(appComponent).build();
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
            snackbarError.setText(getActivity().getString(R.string.limited_template_chat_warning));
            snackbarError.show();
        } else {
            Intent intent = EditTemplateChatActivity.createInstance(getActivity());
            Bundle bundle = new Bundle();
            bundle.putString(InboxMessageConstant.PARAM_MESSAGE, message);
            bundle.putInt(InboxMessageConstant.PARAM_POSITION, position);
            bundle.putInt(InboxMessageConstant.PARAM_NAV, adapter.getList().size() - 1);
            bundle.putStringArrayList(InboxMessageConstant.PARAM_ALL, adapter.getListString());
            if (message == null) {
                bundle.putInt(InboxMessageConstant.PARAM_MODE, CREATE);
            } else {
                bundle.putInt(InboxMessageConstant.PARAM_MODE, EDIT);
            }
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
        snackbarError.setText(ErrorHandler.getErrorMessage(getContext(), errorMessage));
        snackbarError.show();
    }

    @Override
    public void successRearrange() {
        String text = getActivity().getString(R.string.success_rearrange_template_chat);
        snackbarInfo.setText(text);
        snackbarInfo.show();
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
                    snackbarInfo.setText(text);
                    snackbarInfo.show();
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
}
