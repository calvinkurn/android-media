package com.tokopedia.digital.product.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.adapter.NumberListAdapter;
import com.tokopedia.digital.product.model.ClientNumber;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class DigitalSearchNumberFragment extends BasePresenterFragment
        implements NumberListAdapter.OnClientNumberClickListener {

    @BindView(R2.id.recyclerview_number_list)
    RecyclerView rvNumberList;
    @BindView(R2.id.edittext_search_number)
    EditText editTextSearchNumber;
    @BindView(R2.id.btn_clear_number)
    Button btnClearNumber;

    private NumberListAdapter numberListAdapter;

    private List<OrderClientNumber> clientNumbers;

    private static final String ARG_PARAM_EXTRA_NUMBER_LIST = "ARG_PARAM_EXTRA_NUMBER_LIST";
    private static final String ARG_PARAM_EXTRA_NUMBER = "ARG_PARAM_EXTRA_NUMBER";
    private static final String ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER";
    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";

    private ClientNumber clientNumber;
    private String number;

    private OnClientNumberClickListener callback;

    public interface OnClientNumberClickListener {
        void onClientNumberClicked(OrderClientNumber orderClientNumber);
    }

    public static Fragment newInstance(String categoryId, ClientNumber clientNumber, String number,
                                       List<OrderClientNumber> numberList) {
        Fragment fragment = new DigitalSearchNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, categoryId);
        bundle.putParcelable(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumber);
        bundle.putString(ARG_PARAM_EXTRA_NUMBER, number);
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST,
                (ArrayList<? extends Parcelable>) numberList);
        fragment.setArguments(bundle);
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
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {
        callback = (OnClientNumberClickListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        clientNumber = arguments.getParcelable(ARG_PARAM_EXTRA_CLIENT_NUMBER);
        number = arguments.getString(ARG_PARAM_EXTRA_NUMBER);
        clientNumbers = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_search_number_digital;
    }

    @Override
    protected void initView(View view) {
        setClientNumberInputType();

        btnClearNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearchNumber.setText("");
            }
        });

        if (clientNumber != null) {
            editTextSearchNumber.setText(number);
            editTextSearchNumber.setSelection(number.length());
        }
        numberListAdapter = new NumberListAdapter(this, clientNumbers);
        rvNumberList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNumberList.setAdapter(numberListAdapter);
    }

    private void setClientNumberInputType() {
        if (clientNumber.getType().equalsIgnoreCase(ClientNumber.TYPE_INPUT_TEL)
                || clientNumber.getType().equalsIgnoreCase(ClientNumber.TYPE_INPUT_NUMERIC)) {
            editTextSearchNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
            editTextSearchNumber.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        } else {
            editTextSearchNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        }
    }

    @Override
    protected void setViewListener() {
        editTextSearchNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnClearNumber.setVisibility(View.VISIBLE);
                } else {
                    btnClearNumber.setVisibility(View.GONE);
                }
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterData(String query) {
        List<OrderClientNumber> searchClientNumbers = new ArrayList<>();
        for (OrderClientNumber orderClientNumber : clientNumbers) {
            if (orderClientNumber.getClientNumber().contains(query)) {
                searchClientNumbers.add(orderClientNumber);
            }
        }
        if (!searchClientNumbers.isEmpty()) {
            numberListAdapter.setNumbers(searchClientNumbers);
            numberListAdapter.notifyDataSetChanged();
        } else {
            searchClientNumbers.add(new OrderClientNumber.Builder()
                    .clientNumber(query)
                    .build());
            numberListAdapter.setNumbers(searchClientNumbers);
            numberListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onClientNumberClicked(OrderClientNumber orderClientNumber) {
        callback.onClientNumberClicked(orderClientNumber);
    }

}
