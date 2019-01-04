package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class BottomSheetButtonsFragment extends InboxBottomSheetFragment {
    @BindView(R2.id.tv_bad_reason_1)
    TextView tvBadReason1;
    @BindView(R2.id.tv_bad_reason_2)
    TextView tvBadReason2;
    @BindView(R2.id.tv_bad_reason_3)
    TextView tvBadReason3;
    @BindView(R2.id.tv_bad_reason_4)
    TextView tvBadReason4;
    @BindView(R2.id.tv_bad_reason_5)
    TextView tvBadReason5;
    @BindView(R2.id.tv_custom_reason)
    TextView tvCustomReason;
    @BindView(R2.id.tv_label_custom)
    TextView tvLabelCustom;
    @BindView(R2.id.btn_send)
    TextView btnSend;
    @BindView(R2.id.ed_other_reason)
    EditText editText;

    private InboxDetailContract.InboxDetailPresenter mPresenter;
    private View selectedButton;
    private int ratingId;

    @OnClick({R2.id.tv_bad_reason_1,
            R2.id.tv_bad_reason_2,
            R2.id.tv_bad_reason_3,
            R2.id.tv_bad_reason_4,
            R2.id.tv_bad_reason_5,
            R2.id.tv_bad_reason_6,
            R2.id.tv_custom_reason
    })
    void handleClickBadCsat(View v) {
        int id = v.getId();
        int buttonids[] = {R.id.tv_bad_reason_1, R.id.tv_bad_reason_2, R.id.tv_bad_reason_3, R.id.tv_bad_reason_4, R.id.tv_bad_reason_5, R.id.tv_bad_reason_6};
        int selected = Arrays.binarySearch(buttonids, id) + 1;
        switch (selected) {
            case 1:
                setBadrating(v, 1);
                break;
            case 2:
                setBadrating(v, 2);
                break;
            case 3:
                setBadrating(v, 3);
                break;
            case 4:
                setBadrating(v, 4);
                break;
            case 5:
                setBadrating(v, 5);
                break;
            case 6:
                setBadrating(v, 6);
                break;
            default:
                break;
        }
        if (id == R.id.tv_custom_reason) {
            if (selectedButton != null)
                selectedButton.setSelected(false);
            btnSend.setBackgroundResource(R.drawable.rounded_rectangle_grey_solid);
            btnSend.setClickable(false);
            if (v != selectedButton) {
                v.setSelected(true);
                selectedButton = v;
                tvCustomReason.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
            } else {
                selectedButton = null;
                editText.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R2.id.btn_send)
    void onClickSend() {
        if (editText.getVisibility() == View.VISIBLE) {
            mPresenter.sendCustomReason(editText.getText().toString());
            dismiss();
        } else {
            if (ratingId > 0)
                mPresenter.setBadRating(ratingId);
            else
                Toast.makeText(getActivity(), "Please Select a valid reason", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 15) {
                    btnSend.setClickable(true);
                    btnSend.setBackgroundResource(R.drawable.rounded_rectangle_greenbutton_solid);
                } else {
                    btnSend.setBackgroundResource(R.drawable.rounded_rectangle_grey_solid);
                    btnSend.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return rootView;
    }

    private void setBadrating(View v, int ratingId) {
        if (selectedButton != null)
            selectedButton.setSelected(false);
        if (v != selectedButton) {
            v.setSelected(true);
            selectedButton = v;
            this.ratingId = ratingId;
            tvLabelCustom.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            btnSend.setBackgroundResource(R.drawable.rounded_rectangle_greenbutton_solid);
            btnSend.setClickable(true);
        } else {
            selectedButton = null;
            this.ratingId = 0;
            btnSend.setBackgroundResource(R.drawable.rounded_rectangle_grey_solid);
            btnSend.setClickable(false);
        }
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {

    }

    @Override
    public void setPresenter(InboxBaseContract.InboxBasePresenter presenter) {
        if (presenter != null) {
            mPresenter = (InboxDetailContract.InboxDetailPresenter) presenter;
        }
    }
}
