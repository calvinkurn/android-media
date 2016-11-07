package com.tokopedia.tkpd.product.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.product.facade.NetworkParam;
import com.tokopedia.tkpd.product.model.etalase.Etalase;
import com.tokopedia.tkpd.util.ValidationTextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Angga.Prasetiyo on 17/11/2015.
 */
public class DialogToEtalase extends Dialog {
    private final List<Etalase> etalaseList;
    private final Context context;
    private final int productId;

    @Bind(R2.id.ok_button)
    TextView tvYes;
    @Bind(R2.id.spinner_etalase)
    Spinner spinner;
    @Bind(R2.id.etalase_name)
    EditText etNew;

    private final Listener listener;

    public DialogToEtalase(Context context, int productId, List<Etalase> etalases, Listener listener) {
        super(context);
        this.context = context;
        this.etalaseList = etalases;
        this.etalaseList.add(0, new Etalase(null, "Pilih Etalase"));
        this.etalaseList.add(new Etalase(0, "Tambah Etalase"));
        this.productId = productId;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_move_to_etalase);
        ButterKnife.bind(this);
        setCancelable(true);
        ArrayAdapter<Etalase> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, etalaseList);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etNew.setVisibility(position == etalaseList.size() - 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etNew.getText().toString();
                final Etalase selected = (Etalase) spinner.getSelectedItem();
                Map<String, String> param;
                if (selected.getEtalaseId() != null) {
                    if (selected.getEtalaseId() == 0 & newName.isEmpty()) {
                        etNew.setError(context.getString(R.string.error_field_required));
                        return;
                    } else if (selected.getEtalaseId() == 0 & !newName.isEmpty()) {
                        if (isAvailableEtalase(newName)) {
                            etNew.setError(context.getString(R.string.error_etalase_exist));
                            return;
                        } else if (!ValidationTextUtil.isValidText(3, newName)) {
                            etNew.setError(context.getString(R.string.error_min_3_character));
                            return;
                        }
                    }
                } else {
                    listener.onNotSelected();
                    return;
                }

                if (selected.getEtalaseId() == 0) {
                    param = NetworkParam.paramToNewEtalase(productId, newName);
                } else {
                    param = NetworkParam.paramToEtalase(productId, selected);
                }
                listener.onRequestAction(param, productId);
                dismiss();
            }
        });
    }

    private boolean isAvailableEtalase(String newName) {
        for (Etalase etalase : etalaseList) {
            if (etalase.getEtalaseName().contains(newName)) return true;
        }
        return false;
    }


    public interface Listener {

        void onNotSelected();

        void onRequestAction(Map<String, String> param, int productId);
    }


    public static class Builder {
        private Context context;
        private int productId;
        private Listener listener;
        private List<Etalase> etalaseList = new ArrayList<>();

        private Builder() {
        }

        public static Builder aDialogToEtalase() {
            return new Builder();
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder setListener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setEtalases(List<Etalase> etalases) {
            this.etalaseList = etalases;
            return this;
        }

        public Builder but() {
            return aDialogToEtalase()
                    .setContext(context)
                    .setProductId(productId)
                    .setListener(listener)
                    .setEtalases(etalaseList);
        }

        public DialogToEtalase build() {
            return new DialogToEtalase(context, productId,
                    etalaseList, listener);
        }
    }
}