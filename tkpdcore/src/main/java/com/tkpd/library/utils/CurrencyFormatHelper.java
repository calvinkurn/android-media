package com.tkpd.library.utils;

import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * User {@link com.tokopedia.design.utils.CurrencyFormatHelper} or {@link com.tokopedia.design.utils.CurrencyFormatUtil}
 * modified by m.normansyah & steven.f
 * changed "," to "." for rupiah
 */
@Deprecated
public final class CurrencyFormatHelper {
	private static final NumberFormat DollarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
	// this flag intend to block textwatcher to be called recursively
	private static boolean LockTextWatcher = false;

	public static final int RUPIAH = 1;
	public static final int OTHER = -1;

	public static int SetToDollar(EditText et){
		try {
			if(et.length()>0)
				if(!LockTextWatcher){
					LockTextWatcher = true;
					int tempCursorPos = et.getSelectionStart();
					int tempLength = et.length();
					// Handler untuk cent (tanda titik)
					if(et.getText().toString().contains(".")){
						if(et.length() - et.getText().toString().indexOf(".") == 4){
							et.setText(Swapper(et.getText().toString().replace("$", "").replace(",", ""), et.getText().toString().replace("$", "").replace(",", "").indexOf(".")));
						}
						else if(et.length() - et.getText().toString().indexOf(".") == 2){
							et.setText(Swapper(et.getText().toString().replace("$", "").replace(",", ""), et.getText().toString().replace("$", "").replace(",", "").indexOf(".") - 1));
						}
					}
					else if(et.length()>2 && tempCursorPos == et.length() - 2){// Handler untuk ketika menghapus . tidak sengaja
						et.setText(et.getText().toString().substring(0, et.length() - 2) + "." + et.getText().toString().substring(et.length() - 2, et.length()));
						tempCursorPos -= 1;
					}
					et.setText(DollarFormat.format(Double.parseDouble(et.getText().toString().replace(",", ""))).replace("$", ""));
					// Handler untuk tanda koma
					if(et.length() - tempLength == 1) // Untuk majuin cursor ketika nambah koma
					{
						if(et.length()<4)
							tempCursorPos += 1;
						else if(tempCursorPos>-1)
							if(et.getText().charAt(tempCursorPos) != ',') // Untuk mundur ketika mencoba menghapus koma
								tempCursorPos += 1;
					}
					else if(et.length() - tempLength == -1) // Mundurin cursor ketika hapus koma
					{
						tempCursorPos -= 1;
					}

					// Set posisi Cursor
					if(tempCursorPos < et.length()&&tempCursorPos>-1)
						et.setSelection(tempCursorPos);
					else if(tempCursorPos<0)
						et.setSelection(0);
					else
						et.setSelection(et.length());
					LockTextWatcher = false;
				}
		} catch (NumberFormatException e) {
			LockTextWatcher = false;
			e.printStackTrace();
		}
		return et.length();
	}

	private static String Swapper(String source, int first){
		String result = "";
		result = source.substring(0, first);
		result = result + source.charAt(first+1);
		result = result + source.charAt(first);
		result = result + source.subSequence(first+2, source.length());
		return result;
	}

    public static int convertRupiahToInt(String rupiah) {
		try {
        	rupiah = rupiah.replace("Rp", "");
        	rupiah = rupiah.replace(".", "");
        	rupiah = rupiah.replace(" ", "");
			return Integer.parseInt(rupiah);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }

}
