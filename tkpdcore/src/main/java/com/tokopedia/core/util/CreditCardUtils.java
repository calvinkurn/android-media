package com.tokopedia.core.util;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.DatePickerV2;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.core.R;
import com.tokopedia.core.payment.model.responsecartstep1.InstallmentBankOption;
import com.tokopedia.core.payment.model.responsecartstep1.InstallmentTerm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tkpd_Eka on 4/1/2015.
 */
public class CreditCardUtils {

    public CreditCardUtils(Context context, Model model, boolean wsv4) {
        this.context = context;
        this.model = model;
        this.model.isWSV4Data = wsv4;
        formView = View.inflate(context, R.layout.form_credit_card, null);
        setInstallmentView();
        holder = new ViewHolder();
        monthAdapter = SimpleSpinnerAdapter.createAdapter(context, R.array.cc_months);
        generateValidYear();
        yearAdapter = SimpleSpinnerAdapter.createAdapter(context, model.validYear);
        setView();
        setAdapter();
        setViewListener();
        if (!model.forename.equals("")) {
            setViewValue();
            checkChanges();
        }
    }

    public static CreditCardUtils createInstanceWSV4(Context context, FrameLayout form, Model ccModel) {
        ccModel.installment = false;
        CreditCardUtils util = new CreditCardUtils(context, ccModel, true);
        util.frameView = form;
        return util;
    }

    public static CreditCardUtils createInstallmentInstanceWSV4(Context context, FrameLayout frameView,
                                                                Model model,
                                                                List<InstallmentBankOption> installmentBankOptionList) {
        model.installment = true;
        CreditCardUtils util = new CreditCardUtils(context, model, true);
        util.frameView = frameView;
        util.installmentBankOptionList = installmentBankOptionList;
        return util;
    }


    public interface CreditCardUtilListener {
        void onGetVeritransToken(String token);
    }

    public class CreditCardFormException extends Throwable {
        public CreditCardFormException(String message) {
            super(message);
        }
    }

    public static CreditCardUtils createInstance(Context context, FrameLayout frameView, @NonNull Model model) {
        CreditCardUtils util = new CreditCardUtils(context, model);
        util.frameView = frameView;
        return util;
    }

    public static CreditCardUtils createInstallmentInstance(Context context, FrameLayout frameView, @NonNull Model model, ArrayList<String> bankInstallmentOptions) {
        CreditCardUtils util = new CreditCardUtils(context, model);
        util.frameView = frameView;
        util.installmentOptions = bankInstallmentOptions;
        return util;
    }

    public static class Model {
        public String forename = "";
        public String surname;
        public String phone;
        public String postCode;
        public String city;
        public String province;
        public String address;
        public String ccNumber;
        public String ccCVV;
        public String ccMonth;
        public String ccYear;
        public String ccName;
        public String bankId;
        public String bankName;
        public String bankTypeName;
        public String term;
        public boolean installment = false;
        public boolean isWSV4Data = false;
        public int ccEdited = 0;
        public List<String> validYear = new ArrayList<>();

    }

    private class ViewHolder {
        View cvvInfo;
        TextView errorExpiry;
        EditText forename;
        EditText surname;
        EditText phone;
        EditText postCode;
        EditText city;
        EditText province;
        EditText address;
        EditText ccNumber1;
        EditText ccNumber2;
        EditText ccNumber3;
        EditText ccNumber4;
        EditText ccName;
        EditText ccCVV;
        Spinner ccMonth;
        Spinner ccYear;
    }

    private Context context;
    private View formView;
    private View installmentLayout;
    private FrameLayout frameView;
    private ViewHolder holder;
    private Model model;
    private CreditCardUtilListener listener;
    private SimpleSpinnerAdapter monthAdapter;
    private SimpleSpinnerAdapter yearAdapter;
    public ArrayList<String> installmentOptions = new ArrayList<>();
    public List<InstallmentBankOption> installmentBankOptionList = new ArrayList<>();

    public void setListener(CreditCardUtilListener listener) {
        this.listener = listener;
    }

    public CreditCardUtils(Context context, Model model) {
        this.context = context;
        this.model = model;
        initVar();
        setView();
        setAdapter();
        setViewListener();
        if (!model.forename.equals("")) {
            setViewValue();
            checkChanges();
        }

    }

    private void initVar() {
        formView = View.inflate(context, R.layout.form_credit_card, null);
        setInstallmentView();
        holder = new ViewHolder();
        monthAdapter = SimpleSpinnerAdapter.createAdapter(context, R.array.cc_months);
        generateValidYear();
        yearAdapter = SimpleSpinnerAdapter.createAdapter(context, model.validYear);
    }

    private void generateValidYear() {
        int thisYear = Integer.parseInt(DatePickerV2.getToday().getYear());
        model.validYear.add(context.getString(R.string.title_year));
        for (int i = 0; i < 11; i++) {
            model.validYear.add(Integer.toString(thisYear + i));
        }
    }

    private void setAdapter() {
        holder.ccMonth.setAdapter(monthAdapter);
        holder.ccYear.setAdapter(yearAdapter);
    }

    private void setViewValue() {
        holder.forename.setText(MethodChecker.fromHtml(model.forename));
        holder.surname.setText(MethodChecker.fromHtml(model.surname));
        holder.address.setText(MethodChecker.fromHtml(model.address));
        holder.province.setText(MethodChecker.fromHtml(model.province));
        holder.city.setText(MethodChecker.fromHtml(model.city));
        holder.phone.setText(MethodChecker.fromHtml(model.phone));
        holder.postCode.setText(MethodChecker.fromHtml(model.postCode));
        holder.ccName.requestFocus();
    }


    public void insertFormCreditInformation() {
        if (frameView.getChildCount() == 0) {
            frameView.addView(formView);
            addInstallmentView();
        } else
            System.out.println("MAGIC VIEW IS ALREADY ADDED");
    }

    public void removeFormCreditInformation() {
        frameView.removeAllViews();
    }

    private void setView() {
        holder.address = findFieldById(R.id.address);
        holder.city = findFieldById(R.id.city);
        holder.province = findFieldById(R.id.province);
        holder.forename = findFieldById(R.id.forename);
        holder.surname = findFieldById(R.id.surname);
        holder.phone = findFieldById(R.id.phone);
        holder.postCode = findFieldById(R.id.pos);
        holder.ccName = findFieldById(R.id.cc_name);
        holder.ccNumber1 = findFieldById(R.id.cc_number1);
        holder.ccNumber2 = findFieldById(R.id.cc_number2);
        holder.ccNumber3 = findFieldById(R.id.cc_number3);
        holder.ccNumber4 = findFieldById(R.id.cc_number4);
        holder.ccMonth = (Spinner) formView.findViewById(R.id.cc_exp_month);
        holder.ccYear = (Spinner) formView.findViewById(R.id.cc_exp_year);
        holder.ccCVV = findFieldById(R.id.cc_cvv);
        holder.cvvInfo = formView.findViewById(R.id.cvv_info);
        holder.errorExpiry = (TextView) formView.findViewById(R.id.expiry_error);
    }

    private void setViewListener() {
        holder.ccNumber1.addTextChangedListener(onCreditNumberEditListener(holder.ccNumber2));
        holder.ccNumber2.addTextChangedListener(onCreditNumberEditListener(holder.ccNumber3));
        holder.ccNumber3.addTextChangedListener(onCreditNumberEditListener(holder.ccNumber4));
        holder.cvvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogCVVInfo();
            }
        });
    }

    private void createDialogCVVInfo() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(context.getString(R.string.message_cc_cvv));
        dialog.show();
    }

    private TextWatcher onCreditNumberEditListener(final EditText focus) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    focus.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private EditText findFieldById(int id) {
        return (EditText) formView.findViewById(id);
    }

    private void setViewToModel() {
        model.address = holder.address.getText().toString();
        model.ccCVV = holder.ccCVV.getText().toString();
        model.ccName = holder.ccName.getText().toString();
        model.ccNumber = compileCCNumber();
        model.city = holder.city.getText().toString();
        model.forename = holder.forename.getText().toString();
        model.surname = holder.surname.getText().toString();
        model.province = holder.province.getText().toString();
        model.phone = holder.phone.getText().toString();
        model.postCode = holder.postCode.getText().toString();
        model.ccMonth = holder.ccMonth.getSelectedItem().toString();
        model.ccYear = holder.ccYear.getSelectedItem().toString();

    }

    private String compileCCNumber() {
        return holder.ccNumber1.getText().toString()
                + holder.ccNumber2.getText().toString()
                + holder.ccNumber3.getText().toString()
                + holder.ccNumber4.getText().toString();
    }

    private boolean validateForm() {
        try {
            checkFormForEmptyField();
            checkFormMinimumCharacter();
            checkExpiry();
            creditCardValidity();
            return true;
        } catch (CreditCardFormException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkFormForEmptyField() throws CreditCardFormException {
        checkEmptyField(holder.forename);
        checkEmptyField(holder.surname);
        checkEmptyField(holder.phone);
        checkEmptyField(holder.postCode);
        checkEmptyField(holder.city);
        checkEmptyField(holder.province);
        checkEmptyField(holder.address);
        checkEmptyField(holder.ccName);
        checkEmptyField(holder.ccNumber1);
        checkEmptyField(holder.ccNumber2);
        checkEmptyField(holder.ccNumber3);
        checkEmptyField(holder.ccNumber4);
        checkEmptyField(holder.ccCVV);
    }

    private void checkFormMinimumCharacter() throws CreditCardFormException {
        checkMinimumCharacter(holder.ccNumber1, 4);
        checkMinimumCharacter(holder.ccNumber2, 4);
        checkMinimumCharacter(holder.ccNumber3, 4);
        checkMinimumCharacter(holder.ccNumber4, 4);
    }

    private void checkExpiry() throws CreditCardFormException {
        holder.errorExpiry.setVisibility(View.GONE);
        if (holder.ccYear.getSelectedItemPosition() == 0) {
            holder.errorExpiry.setText(context.getString(R.string.error_cc_year));
            holder.errorExpiry.setVisibility(View.VISIBLE);
            throw new CreditCardFormException("Invalid Year");
        }
        if (holder.ccMonth.getSelectedItemPosition() == 0) {
            holder.errorExpiry.setText(context.getString(R.string.error_cc_month));
            holder.errorExpiry.setVisibility(View.VISIBLE);
            throw new CreditCardFormException("Invalid Month");
        }
        if (!checkValidDate()) {
            holder.errorExpiry.setText(context.getString(R.string.error_cc_expires));
            holder.errorExpiry.setVisibility(View.VISIBLE);
            throw new CreditCardFormException("Invalid Month");
        }
    }

    private void checkEmptyField(EditText edit) throws CreditCardFormException {
        edit.setError(null);
        if (edit.length() == 0) {
            edit.setError(context.getString(R.string.error_field_required));
            edit.requestFocus();
            throw new CreditCardFormException("Field Required");
        }
    }

    private void checkMinimumCharacter(EditText edit, int minimumCharacterLength) throws CreditCardFormException {
        edit.setError(null);
        if (edit.length() < minimumCharacterLength) {
            edit.setError(context.getString(R.string.error_cc_number));
            edit.requestFocus();
            throw new CreditCardFormException("Field Required");
        }
    }

    public Model getModel() throws CreditCardFormException {
        if (validateForm()) {
            setViewToModel();
        } else {
            throw new CreditCardFormException("Form Invalid");
        }
        return model;
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            model.ccEdited = 1;
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void creditCardValidity() throws CreditCardFormException {
        if (!checkLuhnAlgorithm()) {
            Toast.makeText(context, "\n" + context.getString(R.string.error_cc_number_please_valid),
                    Toast.LENGTH_LONG).show();
            holder.ccNumber4.requestFocus();
            throw new CreditCardFormException("pastikan nomor kartu kredit anda valid");
        }
    }

    private boolean checkLuhnAlgorithm() {
        int ccInspectedNumber;
        int ccSum = 0;
        String reversedCCNumber = new StringBuffer(compileCCNumber()).reverse().toString();
        for (int i = 0; i < reversedCCNumber.length(); i++) {
            ccInspectedNumber = Character.digit(reversedCCNumber.charAt(i), 10);
            if (i % 2 == 0) {
                ccSum += ccInspectedNumber;
            } else if (i % 2 != 0) {
                ccSum += checkSummaryAlgorithm(ccInspectedNumber);
            }
        }
        if (ccSum % 10 == 0) {
            return true;
        } else return false;
    }

    private int checkSummaryAlgorithm(int ccInspectedNumber) {
        ccInspectedNumber = ccInspectedNumber * 2;
        if (ccInspectedNumber > 9) {
            ccInspectedNumber = ccInspectedNumber - 9;
        }
        return ccInspectedNumber;
    }

    private void checkChanges() {
        holder.forename.addTextChangedListener(textWatcher);
        holder.surname.addTextChangedListener(textWatcher);
        holder.phone.addTextChangedListener(textWatcher);
        holder.phone.addTextChangedListener(textWatcher);
        holder.postCode.addTextChangedListener(textWatcher);
        holder.city.addTextChangedListener(textWatcher);
        holder.province.addTextChangedListener(textWatcher);
        holder.address.addTextChangedListener(textWatcher);
    }

    private boolean checkValidDate() {
        int selectedYear = Integer.parseInt((String) holder.ccYear.getSelectedItem());
        int currentYear = Integer.parseInt(DatePickerV2.getToday().getYear());
        int selectedMonth = Integer.parseInt((String) holder.ccMonth.getSelectedItem());
        int currentMonth = Integer.parseInt(DatePickerV2.getToday().getMonth());
        boolean creditCardValid = true;
        if (selectedYear <= currentYear) {
            if (selectedMonth <= currentMonth) {
                creditCardValid = false;
            }
        }
        return creditCardValid;
    }

    private void setInstallmentView() {
        installmentLayout = formView.inflate(context, R.layout.bank_installment_view, null);
    }

    private void addInstallmentView() {
        if (model.isWSV4Data) {
            if (model.installment) {
                LinearLayout billingView = (LinearLayout) ((RelativeLayout) formView).getChildAt(1);
                billingView.addView(installmentLayout, 1);
                setInstallmentAdapterWSV4(installmentBankOptionList);
            }
        } else {
            if (!installmentOptions.isEmpty()) {
                LinearLayout billingView = (LinearLayout) ((RelativeLayout) formView).getChildAt(1);
                billingView.addView(installmentLayout, 1);
                setInstallmentAdapter(installmentOptions);
                model.installment = true;
            } else {
                model.installment = false;
            }
        }

    }

    private AdapterView.OnItemSelectedListener termSpinnerListener(final ArrayList<String> durationList) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                chooseTerm(durationList, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener bankSpinnerListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                chooseInstallmentBank(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener bankSpinnerListenerWSV4() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                InstallmentBankOption data = installmentBankOptionList.get(position);
                ArrayList<String> durationArray = new ArrayList<>();
                ArrayList<String> monthlyPrice = new ArrayList<>();
                ArrayList<String> durationString = new ArrayList<>();
                SimpleSpinnerAdapter durationSpinnerAdapter;
                Spinner creditPeriodSpinner = (Spinner) installmentLayout.findViewById(R.id.credit_period_spinner);
                for (InstallmentTerm term : data.getInstallmentTerm()) {
                    durationArray.add(term.getDuration());
                    monthlyPrice.add("(" + term.getMonthlyPriceIdr() + ")");
                    durationString.add(term.getDuration() + " Bulan " + term.getMonthlyPriceIdr());
                }
                durationSpinnerAdapter = SimpleSpinnerAdapter.createAdapter(context, durationString);
                creditPeriodSpinner.setAdapter(durationSpinnerAdapter);
                creditPeriodSpinner.setOnItemSelectedListener(termSpinnerListener(durationArray));

                model.bankId = data.getBankId();
                model.bankName = data.getBankName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    //TODO move to Facade
    private void chooseInstallmentBank(int position) {

        if (model.isWSV4Data) {
            getBankArrayList(installmentBankOptionList.get(position).getInstallmentTerm());
            model.bankId = installmentBankOptionList.get(position).getBankId();
            model.bankName = installmentBankOptionList.get(position).getBankName();
            return;
        }
        JSONObject selectedBank;
        try {
            selectedBank = new JSONObject(installmentOptions.get(position));
            getBankArrayList(selectedBank.getJSONArray("installment_term"));
            model.bankId = selectedBank.optString("bank_id");
            model.bankName = selectedBank.optString("bank_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void chooseTerm(ArrayList<String> durationList, int position) {
        model.term = durationList.get(position);
    }

    private void setInstallmentAdapter(ArrayList<String> installmentOptions) {
        Spinner installmentBankSpinner = (Spinner) installmentLayout.findViewById(R.id.installment_bank_spinner);
        SimpleSpinnerAdapter bankSpinnerAdapter;
        ArrayList<String> installmentArray = new ArrayList<>();
        JSONObject selectedBank;
        JSONArray durationJSONArrayList = null;
        for (int i = 0; i < installmentOptions.size(); i++) {
            try {
                selectedBank = new JSONObject(installmentOptions.get(i));
                installmentArray.add(selectedBank.optString("bank_name"));
                if (i == 0) {
                    durationJSONArrayList = selectedBank.getJSONArray("installment_term");
                    model.bankId = selectedBank.optString("bank_id");
                    model.bankName = selectedBank.optString("bank_name");
                    //CommonUtils.UniversalToast(context, "BANK" +model.bankName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //getBankArrayList(durationJSONArrayList);
        bankSpinnerAdapter = SimpleSpinnerAdapter.createAdapter(context, installmentArray);
        installmentBankSpinner.setAdapter(bankSpinnerAdapter);
        installmentBankSpinner.setOnItemSelectedListener(bankSpinnerListener());
    }

    private void setInstallmentAdapterWSV4(List<InstallmentBankOption> installmentOptions) {
        Spinner installmentBankSpinner = (Spinner) installmentLayout.findViewById(R.id.installment_bank_spinner);
        SimpleSpinnerAdapter bankSpinnerAdapter;
        ArrayList<String> installmentArray = new ArrayList<>();
        for (int i = 0; i < installmentOptions.size(); i++) {
            InstallmentBankOption data = installmentOptions.get(i);
            installmentArray.add(data.getBankName());
            if (i == 0) {
                model.bankId = data.getBankId();
                model.bankName = data.getBankName();
            }
        }
        bankSpinnerAdapter = SimpleSpinnerAdapter.createAdapter(context, installmentArray);
        installmentBankSpinner.setAdapter(bankSpinnerAdapter);
        installmentBankSpinner.setOnItemSelectedListener(bankSpinnerListenerWSV4());
    }

    private void getBankArrayList(JSONArray installmentDurationArrayList) {
        ArrayList<String> durationArray = new ArrayList<>();
        ArrayList<String> monthlyPrice = new ArrayList<>();
        ArrayList<String> durationString = new ArrayList<>();
        SimpleSpinnerAdapter durationSpinnerAdapter;
        Spinner creditPeriodSpinner = (Spinner) installmentLayout.findViewById(R.id.credit_period_spinner);
        try {
            for (int i = 0; i < installmentDurationArrayList.length(); i++) {
                durationArray.add(installmentDurationArrayList.getJSONObject(i).getString("duration"));
                monthlyPrice.add("(" + installmentDurationArrayList.getJSONObject(i).getString("monthly_price_idr") + ")");
                durationString.add(durationArray.get(i) + " Bulan " + monthlyPrice.get(i));
                if (i == 0) {
                    model.term = durationArray.get(i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        durationSpinnerAdapter = SimpleSpinnerAdapter.createAdapter(context, durationString);
        creditPeriodSpinner.setAdapter(durationSpinnerAdapter);
        creditPeriodSpinner.setOnItemSelectedListener(termSpinnerListener(durationArray));
    }

    private void getBankArrayList(List<InstallmentTerm> installmentTerms) {
        ArrayList<String> durationArray = new ArrayList<>();
        ArrayList<String> monthlyPrice = new ArrayList<>();
        ArrayList<String> durationString = new ArrayList<>();
        SimpleSpinnerAdapter durationSpinnerAdapter;
        Spinner creditPeriodSpinner = (Spinner) installmentLayout.findViewById(R.id.credit_period_spinner);
        for (int i = 0; i < installmentTerms.size(); i++) {
            durationArray.add(installmentTerms.get(i).getDuration());
            monthlyPrice.add("(" + installmentTerms.get(i).getMonthlyPriceIdr() + ")");
            durationString.add(durationArray.get(i) + " Bulan " + monthlyPrice.get(i));
            if (i == 0) {
                model.term = durationArray.get(i);
            }
        }
        durationSpinnerAdapter = SimpleSpinnerAdapter.createAdapter(context, durationString);
        creditPeriodSpinner.setAdapter(durationSpinnerAdapter);
        creditPeriodSpinner.setOnItemSelectedListener(termSpinnerListener(durationArray));
    }

}
