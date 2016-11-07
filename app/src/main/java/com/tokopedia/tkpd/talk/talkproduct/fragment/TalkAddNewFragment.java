package com.tokopedia.tkpd.talk.talkproduct.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.analytics.UnifyTracking;
import com.tokopedia.tkpd.app.BasePresenterFragment;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.talk.talkproduct.listener.TalkAddNewView;
import com.tokopedia.tkpd.talk.talkproduct.model.AddNewTalkPass;
import com.tokopedia.tkpd.talk.talkproduct.presenter.TalkAddNewPresenter;
import com.tokopedia.tkpd.talk.talkproduct.presenter.TalkAddNewPresenterImpl;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by stevenfredian on 8/2/16.
 */
public class TalkAddNewFragment extends BasePresenterFragment<TalkAddNewPresenter>
                                implements TalkAddNewView{


    @Bind(R2.id.message)
    EditText mMessage;
    @Bind(R2.id.product)
    TextView mProdName;
    @Bind(R2.id.card_view) View cardView;
    @Bind(R2.id.send) View send;
    @Bind(R2.id.container) View container;

    TkpdProgressDialog mProgressDialog;
    String mMessageText;

    public static TalkAddNewFragment createInstance(Bundle extras) {
        TalkAddNewFragment fragment = new TalkAddNewFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }


    @Override
    protected void initialPresenter() {
        this.presenter = new TalkAddNewPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_talk_add_new;
    }

    @Override
    protected void initView(View view) {
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        mProdName.append(Html.fromHtml(getArguments().getString("prod_name")));
    }

    @Override
    protected void setViewListener() {
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessage.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mMessage, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @OnClick(R2.id.send)
    public void submit(){
        KeyboardHandler.DropKeyboard(getActivity(), mMessage);
        addNewTalk();
    }

    private void addNewTalk(){
        boolean cancel;
        View focusView;

        mMessageText = mMessage.getText().toString();

        cancel = validateContent();

        if (cancel) {
            focusView = mMessage;
            focusView.requestFocus();
        } else {
            if(!mProgressDialog.isProgress()){
                mProgressDialog.showDialog();
            }
            presenter.send(getActivity(), getParam());
            send.setEnabled(false);
        }
    }


    private AddNewTalkPass getParam() {
        AddNewTalkPass pass = new AddNewTalkPass();
        pass.setProductID(getArguments().getString("prod_id"));
        pass.setTextMessage(mMessage.getText().toString());
        return pass;
    }

    private boolean validateContent() {
        if(TextUtils.isEmpty(mMessageText)) {
            mMessage.setError(getString(R.string.error_field_required));
            return true;
        }
        if(mMessageText.length() >0 && mMessageText.length() < 5) {
            mMessage.setError(getString(R.string.error_min_5_character));
            return true;
        }
        return false;
    }


    @Override
    public void onSuccessAdd() {
        Bundle bundle = new Bundle();
        bundle.putString("message", mMessage.getText().toString());
        getActivity().setResult(TalkProductFragment.RESULT_ADD, new Intent().putExtras(bundle));
        send.setEnabled(true);
        mProgressDialog.dismiss();
        UnifyTracking.eventTalkSuccessSend();
        getActivity().finish();
    }

    @Override
    public void onErrorAdd(String error) {
        mProgressDialog.dismiss();
//				Toast.makeText(TalkAddNew.this, error, Toast.LENGTH_LONG).show();
        NetworkErrorHelper.showSnackbar(getActivity(),error);
        send.setEnabled(true);
    }


}
