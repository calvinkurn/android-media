package com.tokopedia.digital.product.view.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public abstract class BaseDigitalRadioChooserView<T> extends RelativeLayout {
    protected List<T> dataList;
    protected T dataSelected;
    protected ActionListener<T> actionListener;
    protected Context context;

    public BaseDigitalRadioChooserView(Context context) {
        super(context);
        this.context = context;
        initialView(context, null, 0);
    }

    public BaseDigitalRadioChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialView(context, attrs, 0);
    }

    public BaseDigitalRadioChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialView(context, attrs, defStyleAttr);
    }

    private void initialView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(getHolderLayoutId(), this, true);
        ButterKnife.bind(this);
        initialViewListener();
        disableLabelText();
    }

    public void setActionListener(ActionListener<T> actionListener) {
        this.actionListener = actionListener;
    }

    protected abstract void initialViewListener();

    protected abstract int getHolderLayoutId();

    public abstract void enableLabelText(String labelText);

    public abstract void disableLabelText();

    public abstract void enableError(String errorMessage);

    public abstract void disableError();

    public abstract void renderInitDataList(List<T> data, String defaultOperatorId);

    public abstract void renderUpdateDataSelected(T data);

    public interface ActionListener<Z> {

        void onUpdateDataDigitalRadioChooserSelectedRendered(Z data);

        void tracking();

    }
}
