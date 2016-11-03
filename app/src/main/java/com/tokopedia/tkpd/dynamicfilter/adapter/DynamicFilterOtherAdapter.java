package com.tokopedia.tkpd.dynamicfilter.adapter;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.discovery.adapter.ProductAdapter;
import com.tokopedia.tkpd.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by noiz354 on 7/13/16.
 */
public class DynamicFilterOtherAdapter extends ProductAdapter {

    interface CONSTANT {
        String CHECKBOX = "checkbox";
        String TEXTBOX = "textbox";

        int TEXT_BOX_MODEL_TYPE = 129_648;
        int CHECK_BOX_MODEL_TYPE = 743_271;
    }

    private Filter filter;
    private static final String TAG = DynamicFilterOtherAdapter.class.getSimpleName();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CONSTANT.TEXT_BOX_MODEL_TYPE:
                return createViewTextBox(parent);
            case CONSTANT.CHECK_BOX_MODEL_TYPE:
                return createViewCheckBox(parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case CONSTANT.TEXT_BOX_MODEL_TYPE:
                ((TextBoxViewHolder) holder).bindData(getData().get(position), position);
                break;
            case CONSTANT.CHECK_BOX_MODEL_TYPE:
                ((CheckBoxViewHolder) holder).bindData(getData().get(position), position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    public static TextBoxViewHolder createViewTextBox(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_box_layout, parent, false);
        return new TextBoxViewHolder(itemLayoutView);
    }

    public CheckBoxViewHolder createViewCheckBox(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_box_layout, parent, false);
        return new CheckBoxViewHolder(itemLayoutView);
    }

    @Override
    protected int isInType(RecyclerViewItem recyclerViewItem) {
        switch (recyclerViewItem.getType()) {
            case CONSTANT.TEXT_BOX_MODEL_TYPE:
            case CONSTANT.CHECK_BOX_MODEL_TYPE:
                return recyclerViewItem.getType();
        }

        return super.isInType(recyclerViewItem);
    }

    public DynamicFilterOtherAdapter(Context context, List<RecyclerViewItem> data) {
        super(context, data);
    }

    public static abstract class BaseRecylerViewHolder extends RecyclerView.ViewHolder {

        public BaseRecylerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public abstract void bindData(RecyclerViewItem recyclerViewItem, int position);
    }

    public static class TextBoxViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_box_container)
        TextInputLayout TextBoxContainer;

        @Bind(R.id.text_box)
        EditText textBox;

        TextBoxModel textBoxModel;

        public TextBoxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        //        @Override
        public void bindData(RecyclerViewItem recyclerViewItem, int position) {
            if (recyclerViewItem != null && recyclerViewItem instanceof TextBoxModel) {
                bindData2((TextBoxModel) recyclerViewItem, position);
            }
        }

        public void bindData2(final TextBoxModel textBoxModel, int position) {
            this.textBoxModel = textBoxModel;
            Log.d(TAG, "textBoxModel " + textBoxModel.text + " first time " + textBoxModel.isFirstTime + " key " + textBoxModel.key + " option " + textBoxModel.option);
            textBox.setHint(textBoxModel.text);
            textBox.setText("");
            Context context = itemView.getContext();
            if (textBoxModel.isFirstTime && context != null && context instanceof DynamicFilterView) {
                String textInput = ((DynamicFilterView) context).getTextInput(textBoxModel.key);
                if (textInput != null) {
                    textBox.setText(textInput);
                }
                textBoxModel.isFirstTime = false;
            }
            textBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    TextBoxViewHolder.this.textBoxModel.text = s.toString();

                    Context context = itemView.getContext();
                    if (context != null && context instanceof DynamicFilterView) {
                        if (!s.toString().equals("")) {
                            ((DynamicFilterView) context).putSelectedFilter(TextBoxViewHolder.this.textBoxModel.option.getKey(), s.toString());
                            ((DynamicFilterView) context).saveTextInput(textBoxModel.key, s.toString());
                        } else {
                            ((DynamicFilterView) context).removeSelecfedFilter(TextBoxViewHolder.this.textBoxModel.option.getKey());
                            ((DynamicFilterView) context).removeTextInput(textBoxModel.key);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public void reset() {
        for (RecyclerViewItem item : getData()) {
            if (item instanceof CheckBoxModel) {
                CheckBoxModel model = (CheckBoxModel) item;
                model.isChecked = false;
            }
            if (item instanceof TextBoxModel) {
                TextBoxModel model = (TextBoxModel) item;
                if (model.option.getKey().equals("pmin")) {
                    model.text = "Harga Minimum";
                } else {
                    model.text = "Harga Maksimum";
                }
                model.isFirstTime = true;
            }
        }
        notifyDataSetChanged();
    }

    public class CheckBoxViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.checkbox)
        CheckBox checkBox;

        CheckBoxModel checkBoxModel;

        public CheckBoxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        //        @Override
        public void bindData(RecyclerViewItem recyclerViewItem, int position) {
            if (recyclerViewItem != null && recyclerViewItem instanceof CheckBoxModel) {
                bindData2((CheckBoxModel) recyclerViewItem, position);
            }
        }

        public void bindData2(final CheckBoxModel checkBoxModel, final int positon) {
            this.checkBoxModel = checkBoxModel;
            Context context = itemView.getContext();
            checkBox.setText(checkBoxModel.option.getName());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBoxViewHolder.this.checkBoxModel.isChecked = isChecked;
                    String key = CheckBoxViewHolder.this.checkBoxModel.option.getKey();
                    Context context = itemView.getContext();
                    if (context != null && context instanceof DynamicFilterView) {
                        if (isChecked) {
                            ((DynamicFilterView) context).putSelectedFilter(key, getSelectedIds());
                            ((DynamicFilterView) context).saveCheckedPosition(checkBoxModel.key, isChecked);
                        } else {
                            ((DynamicFilterView) context).removeCheckedPosition(checkBoxModel.key);
                            if(getSelectedIds().isEmpty()) {
                                ((DynamicFilterView) context).removeSelecfedFilter(key);
                            } else {
                                ((DynamicFilterView) context).putSelectedFilter(key, getSelectedIds());
                            }
                        }
                    }
                }
            });
            if (checkBoxModel.isFirstTime && context != null && context instanceof DynamicFilterView) {
                Boolean isChecked = ((DynamicFilterView) context).getCheckedPosition(checkBoxModel.key);
                if (isChecked != null && isChecked) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                checkBoxModel.isFirstTime = false;
            } else {
                checkBox.setChecked(checkBoxModel.isChecked);
            }
        }
    }

    private String getSelectedIds() {
        StringBuffer buffer = new StringBuffer();
        for (RecyclerViewItem item : getData()) {
            if (item instanceof CheckBoxModel) {
                CheckBoxModel checkBox = (CheckBoxModel) item;
                if (checkBox.isChecked) {
                    buffer.append(checkBox.option.getValue()).append(",");
                }
            }
        }
        if(buffer.length()>0) {
            return buffer.substring(0, buffer.length() - 1);
        } else {
            return buffer.toString();
        }
    }

    public static class TextBoxModel extends RecyclerViewItem {
        public String text;
        public String key;
        private DynamicFilterModel.Option option;
        public boolean isFirstTime = true;

        public TextBoxModel() {
            setType(CONSTANT.TEXT_BOX_MODEL_TYPE);
        }

        public TextBoxModel(DynamicFilterModel.Option option) {
            this();
            if(option.getName().contains("Harga Maximum")){
                option.setName("Harga Maksimum");
            }
            String formatText = "%s_%s_%s";
            String format = String.format(formatText, option.getName(), option.getKey(), option.getValue());
            this.key = format;
            this.option = option;
            text = option.getName();
        }
    }

    public static class CheckBoxModel extends RecyclerViewItem {
        private boolean isChecked = false;
        public String key;
        private DynamicFilterModel.Option option;
        public boolean isFirstTime = true;

        public CheckBoxModel() {
            setType(CONSTANT.CHECK_BOX_MODEL_TYPE);
        }

        public CheckBoxModel(DynamicFilterModel.Option option) {
            this();
            String formatText = "%s_%s_%s";
            String format = String.format(formatText, option.getName(), option.getKey(), option.getValue());
            this.key = format;
            this.option = option;
        }
    }

    public static boolean isCheckbox(String inputType) {
        return inputType.equals(CONSTANT.CHECKBOX);
    }

    public static boolean isTextBox(String inputType) {
        return inputType.equals(CONSTANT.TEXTBOX);
    }

    public static RecyclerViewItem convertTo(DynamicFilterModel.Option option) {
        if (isCheckbox(option.getInputType())) {
            return new CheckBoxModel(option);
        } else if (isTextBox(option.getInputType())) {
            return new TextBoxModel(option);
        } else {
            return null;
        }
    }

}
