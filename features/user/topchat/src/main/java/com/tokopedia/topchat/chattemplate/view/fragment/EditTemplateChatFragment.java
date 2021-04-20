package com.tokopedia.topchat.chattemplate.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics;
import com.tokopedia.topchat.chattemplate.di.DaggerTemplateChatComponent;
import com.tokopedia.topchat.chattemplate.di.TemplateChatModule;
import com.tokopedia.topchat.chattemplate.view.listener.EditTemplateChatContract;
import com.tokopedia.topchat.chattemplate.view.presenter.EditTemplateChatPresenter;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.util.Events;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity.PARAM_IS_SELLER;

/**
 * Created by stevenfredian on 12/22/17.
 */

public class EditTemplateChatFragment extends BaseDaggerFragment
        implements EditTemplateChatContract.View {

    private static final int MAX_CHAR = 200;
    private static final int DISABLE_DELETE = 130;
    private static final int ENABLE_DELETE = 255;

    private TextView counter;
    private TextView error;
    private TextView submit;
    private EditText editText;

    private List list;
    private String message;
    private Observable<Integer> counterObservable;
    private int allowDelete;
    private Boolean isSeller;

    public static EditTemplateChatFragment createInstance(Bundle extras) {
        EditTemplateChatFragment fragment = new EditTemplateChatFragment();
        fragment.setArguments(extras);
        fragment.isSeller = extras.getBoolean(PARAM_IS_SELLER);
        return fragment;
    }

    @Inject
    EditTemplateChatPresenter presenter;

    @Inject
    ChatTemplateAnalytics analytics;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments().getInt(InboxMessageConstant.PARAM_MODE) == TemplateChatFragment.CREATE) {
            setHasOptionsMenu(false);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete_template, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_organize);
        if (getArguments().getInt(InboxMessageConstant.PARAM_NAV) == 1) {
            allowDelete = DISABLE_DELETE;
            item.getIcon().setAlpha(DISABLE_DELETE);
        } else {
            allowDelete = ENABLE_DELETE;
            item.getIcon().setAlpha(ENABLE_DELETE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_organize) {
            if (allowDelete == ENABLE_DELETE) {
                analytics.trackDeleteTemplateChat();
                showDialogDelete();
            } else {
                showError(new MessageErrorException(getActivity().getString(R.string
                        .minimum_template_chat_warning)));
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showDialogDelete() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_chat_template)
                .setMessage(R.string.forever_deleted_template)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        analytics.trackConfirmDeleteTemplateChat();
                        presenter.deleteTemplate(getArguments().getInt(InboxMessageConstant.PARAM_POSITION));
                        dialog.dismiss();
                    }

                })
                .setNegativeButton(com.tokopedia.resources.common.R.string.general_label_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_template_chat, container, false);

        counter = rootView.findViewById(R.id.counter);
        error = rootView.findViewById(R.id.error);
        submit = rootView.findViewById(R.id.submit);
        editText = rootView.findViewById(R.id.edittext);

        presenter.attachView(this);
        presenter.setMode(isSeller);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        message = getArguments().getString(InboxMessageConstant.PARAM_MESSAGE);
        list = getArguments().getStringArrayList(InboxMessageConstant.PARAM_ALL);


        editText.setText(message);
        if (message != null) {
            editText.setSelection(message.length());
        }

        counterObservable = Events.text(editText).map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return s.length();
            }
        });

        Action1<Integer> onNextAction = integer -> getActivity().runOnUiThread(() -> {
            showErrorAndProceed(integer, submit);
            counter.setText(String.format("%d/%d", integer, MAX_CHAR));
        });

        Action1<Throwable> onError = throwable -> throwable.printStackTrace();

        counterObservable.subscribe(onNextAction, onError);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mode = getMode();
                switch (mode) {
                    case TemplateChatFragment.CREATE:
                        analytics.trackCreateSaveTemplateChat();
                        break;
                    case TemplateChatFragment.EDIT:
                        analytics.trackEditSaveTemplateChat();
                        break;
                }
                presenter.submitText(editText.getText().toString(), message, list);
            }
        });
    }

    private int getMode() {
        if (getArguments() == null) return -2;
        return getArguments().getInt(InboxMessageConstant.PARAM_MODE, -2);
    }

    private void showErrorAndProceed(Integer integer, TextView proceed) {
        if (integer == 0) {
            canProceed(false, proceed);
        } else if (integer > 0 && integer < 5) {
            error.setText(getActivity().getString(R.string.minimal_char_template));
            error.setVisibility(View.VISIBLE);
            canProceed(false, proceed);
        } else if (integer > MAX_CHAR) {
            error.setText(getActivity().getString(R.string.maximal_char_template, MAX_CHAR));
            error.setVisibility(View.VISIBLE);
            canProceed(false, proceed);
        } else {
            error.setVisibility(View.GONE);
            canProceed(true, proceed);
        }
    }

    public void canProceed(boolean can, TextView proceed) {
        proceed.setEnabled(can);
        if (can) {
            proceed.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_G400), PorterDuff.Mode.SRC_IN);
            proceed.setTextColor(MethodChecker.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
        } else {
            proceed.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N100), PorterDuff.Mode.SRC_IN);
            proceed.setTextColor(MethodChecker.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N200));
        }
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
    public void onResult(EditTemplateUiModel editTemplateViewModel, int index, String s) {
        analytics.eventClickTemplate();
        Intent intent = new Intent();
        intent.putExtra(TemplateChatFragment.INDEX_RESULT, index);
        intent.putExtra(TemplateChatFragment.LIST_RESULT, s);
        intent.putExtra("enabled", editTemplateViewModel.isEnabled());
        intent.putExtra(TemplateChatFragment.MODE_RESULT, getArguments().getInt(InboxMessageConstant.PARAM_MODE));
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onResult(EditTemplateUiModel editTemplateViewModel, int index) {
        Intent intent = new Intent();
        intent.putExtra(TemplateChatFragment.INDEX_RESULT, index);
        intent.putExtra(TemplateChatFragment.MODE_RESULT, TemplateChatFragment.DELETE);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void showError(Throwable error) {
        SnackbarManager.make(getActivity(), ErrorHandler.getErrorMessage(getContext(), error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
