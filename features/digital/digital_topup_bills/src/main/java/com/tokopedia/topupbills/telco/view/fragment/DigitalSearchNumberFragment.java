package com.tokopedia.topupbills.telco.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.topupbills.R;
import com.tokopedia.topupbills.telco.data.constant.TelcoFavNumberType;
import com.tokopedia.topupbills.telco.view.adapter.NumberListAdapter;
import com.tokopedia.topupbills.telco.view.model.DigitalFavNumber;
import com.tokopedia.topupbills.telco.view.model.DigitalOrderClientNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class DigitalSearchNumberFragment extends BaseDaggerFragment
        implements NumberListAdapter.OnClientNumberClickListener {

    private RecyclerView rvNumberList;
    private EditText editTextSearchNumber;
    private Button btnClearNumber;

    private NumberListAdapter numberListAdapter;

    private List<DigitalOrderClientNumber> clientNumbers;

    private static final String ARG_PARAM_EXTRA_NUMBER_LIST = "ARG_PARAM_EXTRA_NUMBER_LIST";
    private static final String ARG_PARAM_EXTRA_NUMBER = "ARG_PARAM_EXTRA_NUMBER";
    private static final String ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER";

    private DigitalFavNumber clientNumber;
    private String number;

    private OnClientNumberClickListener callback;

    @Override
    protected void initInjector() {

    }

    public interface OnClientNumberClickListener {
        void onClientNumberClicked(DigitalOrderClientNumber orderClientNumber);
    }

    public static Fragment newInstance(DigitalFavNumber clientNumber, String number) {
        Fragment fragment = new DigitalSearchNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumber);
        bundle.putString(ARG_PARAM_EXTRA_NUMBER, number);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        callback = (OnClientNumberClickListener) context;
    }

    public void setupArguments(Bundle arguments) {
        clientNumber = arguments.getParcelable(ARG_PARAM_EXTRA_CLIENT_NUMBER);
        number = arguments.getString(ARG_PARAM_EXTRA_NUMBER);
        clientNumbers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_number_digital, container, false);
        initView(view);
        setViewListener();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
    }

    private void initView(View view) {
        rvNumberList = view.findViewById(R.id.recyclerview_number_list);
        editTextSearchNumber = view.findViewById(R.id.edittext_search_number);
        btnClearNumber = view.findViewById(R.id.btn_clear_number);

        setClientNumberInputType();

        if (TextUtils.isEmpty(number)) {
            btnClearNumber.setVisibility(View.GONE);
        }

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
        if (clientNumber != null && (clientNumber.getType().equalsIgnoreCase(TelcoFavNumberType.TYPE_INPUT_TEL)
                || clientNumber.getType().equalsIgnoreCase(TelcoFavNumberType.TYPE_INPUT_NUMERIC))) {
            editTextSearchNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
            editTextSearchNumber.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        } else {
            editTextSearchNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        }
    }

    private void setViewListener() {
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

        editTextSearchNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    DigitalOrderClientNumber orderClientNumber = findNumber(textView.getText().toString(),
                            numberListAdapter.getClientNumbers());
                    if (orderClientNumber != null) {
                        callback.onClientNumberClicked(orderClientNumber);
                    } else {
                        callback.onClientNumberClicked(new DigitalOrderClientNumber.Builder()
                                .clientNumber(textView.getText().toString())
                                .build());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void filterData(String query) {
        List<DigitalOrderClientNumber> searchClientNumbers = new ArrayList<>();
        if (!TextUtils.isEmpty(query) & !isContain(query, clientNumbers)) {
            searchClientNumbers.add(new DigitalOrderClientNumber.Builder()
                    .clientNumber(query)
                    .build());
        }
        for (DigitalOrderClientNumber orderClientNumber : clientNumbers) {
            if (orderClientNumber.getClientNumber().contains(query)) {
                searchClientNumbers.add(orderClientNumber);
            }
        }
        numberListAdapter.setNumbers(searchClientNumbers);
        numberListAdapter.notifyDataSetChanged();
    }

    private boolean isContain(String number, List<DigitalOrderClientNumber> clientNumbers) {
        boolean found = false;
        for (DigitalOrderClientNumber orderClientNumber : clientNumbers) {
            if (orderClientNumber.getClientNumber().equalsIgnoreCase(number)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private DigitalOrderClientNumber findNumber(String number, List<DigitalOrderClientNumber> clientNumbers) {
        DigitalOrderClientNumber foundClientNumber = null;
        for (DigitalOrderClientNumber orderClientNumber : clientNumbers) {
            if (orderClientNumber.getClientNumber().equalsIgnoreCase(number)) {
                foundClientNumber = orderClientNumber;
                break;
            }
        }
        return foundClientNumber;
    }

    @Override
    public void onClientNumberClicked(DigitalOrderClientNumber orderClientNumber) {
        callback.onClientNumberClicked(orderClientNumber);
    }

}
