package com.tokopedia.translator.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import com.tokopedia.translator.R;
import com.tokopedia.translator.repository.source.GetDataService;
import com.tokopedia.translator.repository.source.RetrofitClientInstance;
import com.tokopedia.unifycomponents.TextFieldUnify;

import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.NumberFormat;
import java.util.Locale;

public class TranslatorSettingView extends FrameLayout {
    public static String IS_ENABLE = "tl_is_enable";
    public static String API_KEY = "tl_api_key";
    public static String SOURCE_LANGUAGE = "tl_source_language";
    public static String DESTINATION_LANGUAGE = "tl_destination_language";
    public static String DELIM_LANG_CODE = "-";
    public static String CHARS_COUNT = "chars_count";
    private boolean mIsSelectedOrigin = false;
    private boolean mIsSelectedDestination = false;


    public TranslatorSettingView(Context context) {
        super(context);
        initView();
    }

    public TranslatorSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TranslatorSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        final View view = inflate(getContext(), R.layout.tl_layout_config, null);
        Switch switchTranslator = view.findViewById(R.id.sw_translate);
        switchTranslator.setChecked(SharedPrefsUtils.INSTANCE.getBooleanPreference(getContext(), IS_ENABLE, false));

        String currKey = SharedPrefsUtils.INSTANCE.getStringPreference(getContext(), API_KEY);

        if (!TextUtils.isEmpty(currKey)) {
            TextView textApiKey = view.findViewById(R.id.text_curr_key);
            textApiKey.setText(currKey);
        }

        TextView textLangVal = view.findViewById(R.id.text_val);
        CommonUtil.setText(textLangVal, "Currently selected languages is from <b><i>"
                + SharedPrefsUtils.INSTANCE.getStringPreference(getContext(), SOURCE_LANGUAGE).split(DELIM_LANG_CODE)[0]
                + "</i></b> to <b><i>"
                + SharedPrefsUtils.INSTANCE.getStringPreference(getContext(), DESTINATION_LANGUAGE).split(DELIM_LANG_CODE)[0]
                + "</i></b>");

        TextView textCharCount = view.findViewById(R.id.text_count_char);
        CommonUtil.setText(textCharCount, "Total translated text: " + NumberFormat.getNumberInstance(Locale.US).format(SharedPrefsUtils.INSTANCE.getIntegerPreference(getContext(), CHARS_COUNT, 0)));

        switchTranslator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getContext(), b ? "Translator enabled" : "Translator disabled", Toast.LENGTH_SHORT).show();
                SharedPrefsUtils.INSTANCE.setBooleanPreference(getContext(), IS_ENABLE, b);
            }
        });

        view.findViewById(R.id.btn_save_key).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
                final TextFieldUnify editApiKey = view.findViewById(R.id.edit_key);
                if (editApiKey == null || editApiKey.getTextFieldInput().getText() == null || editApiKey.getTextFieldInput().getText().toString().isEmpty()) {
                    return;
                }

                GetDataService service = RetrofitClientInstance.Companion.getRetrofitInstance().create(GetDataService.class);
                Call<String> call = service.checkApiKeyValidity(editApiKey.getTextFieldInput().getText().toString(), "hello world");
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strJson = response.body();
                                JSONObject data = new JSONObject(strJson);
                                int resCode = data.getInt("code");
                                CommonUtil.showToastWithUIUpdate(getContext()
                                        , "API-KEY has been saved successfully"
                                        , (TextView) findViewById(R.id.text_curr_key)
                                        , editApiKey.getTextFieldInput().getText().toString().trim());

                                SharedPrefsUtils.INSTANCE.setStringPreference(getContext(), API_KEY, editApiKey.getTextFieldInput().getText().toString().trim());

                            } catch (JSONException e) {
                                CommonUtil.showToast(getContext()
                                        , "Something went wrong, please try later");
                            }
                        } else {
                            CommonUtil.showToast(getContext()
                                    , "Seems invalid API-KEY entered, please try again with correct KEY");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        CommonUtil.showToast(getContext()
                                , "Something went wrong, please try later");
                    }
                });
                SharedPrefsUtils.INSTANCE.setStringPreference(getContext(), API_KEY, editApiKey.getTextFieldInput().getText().toString().trim());
            }
        });

        Spinner spOrigin = view.findViewById(R.id.list_origin);
        spOrigin.setEnabled(false);
        spOrigin.setClickable(false);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.supported_languages, R.layout.tl_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner

        Spinner spDestination = view.findViewById(R.id.list_destination);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDest = ArrayAdapter.createFromResource(getContext(),
                R.array.supported_languages, R.layout.tl_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterDest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spDestination.setAdapter(adapter);
        spOrigin.setAdapter(adapter);
        spOrigin.setSelection(1,false);

        spOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mIsSelectedOrigin) {
                    String[] arrSupportedLanguages = getResources().getStringArray(R.array.supported_languages);
                    SharedPrefsUtils.INSTANCE.setStringPreference(getContext(), SOURCE_LANGUAGE, arrSupportedLanguages[i]);

                    CommonUtil.showToastWithUIUpdate(getContext()
                            , arrSupportedLanguages[i].split(DELIM_LANG_CODE)[0] + " set as a Origin language."
                            , (TextView) findViewById(R.id.text_val)
                            , "Currently selected languages is from <b><i>"
                                    + arrSupportedLanguages[i].split(DELIM_LANG_CODE)[0]
                                    + "</i></b> to <b><i>"
                                    + SharedPrefsUtils.INSTANCE.getStringPreference(getContext(), DESTINATION_LANGUAGE).split(DELIM_LANG_CODE)[0]
                                    + "</i></b>");
                } else {
                    mIsSelectedOrigin = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mIsSelectedDestination) {
                    String[] arrSupportedLanguages = getResources().getStringArray(R.array.supported_languages);
                    SharedPrefsUtils.INSTANCE.setStringPreference(getContext(), DESTINATION_LANGUAGE, arrSupportedLanguages[i]);

                    CommonUtil.showToastWithUIUpdate(getContext()
                            , arrSupportedLanguages[i].split("-")[0] + " set as a Destination language."
                            , (TextView) findViewById(R.id.text_val)
                            , "Currently selected languages is from <b><i>"
                                    + SharedPrefsUtils.INSTANCE.getStringPreference(getContext(), SOURCE_LANGUAGE).split(DELIM_LANG_CODE)[0]
                                    + "</i></b> to <b><i>"
                                    + arrSupportedLanguages[i].split(DELIM_LANG_CODE)[0] + "</i></b>");
                } else {
                    mIsSelectedDestination = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addView(view);
    }

}
