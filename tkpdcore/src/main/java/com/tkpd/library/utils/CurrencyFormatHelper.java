package com.tkpd.library.utils;

import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Currency;
import java.util.Locale;

/**
 * modified by m.normansyah & steven.f
 * changed "," to "." for rupiah
 */
public final class CurrencyFormatHelper {
	private static final NumberFormat RupiahFormat = NumberFormat.getCurrencyInstance(Locale.US);
	private static final NumberFormat DollarFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
	// this flag intend to block textwatcher to be called recursively
	private static boolean LockTextWatcher = false;

	public static final int RUPIAH = 1;
	public static final int OTHER = -1;


	public static String ConvertToRupiah(String string){
		try{
			String inrupiah;
			inrupiah = RupiahFormat.format(Long.parseLong(string.replace("Rp", ""))).replace("$", "").replace("Rp", "").replace(".00", "");
			return inrupiah;
		}
		catch(NumberFormatException e){
			e.printStackTrace();
			return "Exception raised";
		}
	}

	public static String ConvertToDollar(String string){
		try {
			String indollar;
			indollar = DollarFormat.format(Double.parseDouble(string)).replace("$", "");
			return indollar;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "Exception raised";
		}
	}

	/**
	 * see setToRupiahCheckPrefix to check prefix in edit text
	 */
	@Deprecated
	public static void SetToRupiah(EditText et){
		try {
			if(et.length()>0 && !LockTextWatcher){
				LockTextWatcher = true;
				int tempCursorPos = et.getSelectionStart();
				int tempLength = et.length();
				et.setText(RupiahFormat.format(Long.parseLong(et.getText().toString().replace("$", "").replace(".00", "").replace(".0", "").replace(",", "").replace(".", ""))).replace("$", "").replace(".00", "").replace(".0", ""));
				// Handler untuk tanda koma
				if(et.length() - tempLength == 1) // Untuk majuin cursor ketika nambah koma
				{
					if(et.length()<4)
						tempCursorPos += 1;
					else if(et.getText().charAt(tempCursorPos) != '.') // Untuk mundur ketika mencoba menghapus koma
						tempCursorPos += 1;
				}
				else if(et.length() - tempLength == -1) // Mundurin cursor ketika hapus koma
				{
					tempCursorPos -= 1;
				}
				else if(et.length()>3 && tempCursorPos<et.length() && tempCursorPos>0)
					if(et.getText().charAt(tempCursorPos-1) == '.'){ // Mundurin cursor ketika menambah digit dibelakang koma
						tempCursorPos -= 1;
					}

				// Set posisi cursor
				if(tempCursorPos<et.length() && tempCursorPos > -1)
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
	}

	public static void setToRupiahCheckPrefix(EditText et){
		try {
			int noFirstCharToIgnore = countPrefixCurrency(et.getText().toString());
			if(et.length()>noFirstCharToIgnore && !LockTextWatcher){
				LockTextWatcher = true;
				int tempCursorPos = et.getSelectionStart();
				int tempLength = et.length();
				et.setText(RupiahFormat.format(Long.parseLong(
						et.getText().toString()
								.substring(noFirstCharToIgnore)
								.replace("$", "")
								.replace(".00", "")
								.replace(".0", "")
								.replace(",", "")
								.replace(".", "")))
								.replace("$", "")
								.replace(".00", "")
								.replace(".0", ""));
				// Handler untuk tanda koma
				if(et.length() - tempLength == 1) // Untuk majuin cursor ketika nambah koma
				{
					if(et.length()< (4 + noFirstCharToIgnore)) {
						tempCursorPos += 1;
					}
					else if(et.getText().charAt(tempCursorPos) != '.') { // Untuk mundur ketika mencoba menghapus koma
						tempCursorPos += 1;
					}
				}
				else if(et.length() - tempLength == -1) // Mundurin cursor ketika hapus koma
				{
					tempCursorPos -= 1;
				}
				else if(et.length()>(3 + noFirstCharToIgnore)
						&& tempCursorPos < et.length()
						&& tempCursorPos > noFirstCharToIgnore) {
					if (et.getText().charAt(tempCursorPos - 1) == '.') { // Mundurin cursor ketika menambah digit dibelakang koma
						tempCursorPos -= 1;
					}
				}

				// Set posisi cursor
				if(tempCursorPos < et.length() && tempCursorPos > (-1+noFirstCharToIgnore) )
					et.setSelection(tempCursorPos);
				else if(tempCursorPos< noFirstCharToIgnore )
					et.setSelection(noFirstCharToIgnore);
				else
					et.setSelection(et.length());
				LockTextWatcher = false;
			}
		} catch (NumberFormatException e) {
			LockTextWatcher = false;
			e.printStackTrace();
		}
	}

	public static int countPrefixCurrency(String string) {
		int count = 0;
		for (int i=0, sizei = string.length();i<sizei ; i++) {
			char charString = string.charAt(i);
			if (Character.isDigit(charString)){
				break;
			}
			else {
				count++;
			}
		}
		return count;
	}

	public static String RemoveNonNumeric(String string){
		String numeric;
		numeric = string.replace(",", "");
		return numeric;
	}

	public static String removeCurrencyPrefix(String string){
		if (string == null) return null;
		int count = 0;
		for (int i=0, sizei = string.length();i<sizei ; i++) {
			char charString = string.charAt(i);
			if (Character.isDigit(charString)){
				break;
			}
			else {
				count++;
			}
		}
		return string.substring(count);
	}

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
        rupiah = rupiah.replace("Rp", "");
        rupiah = rupiah.replace(".", "");
        rupiah = rupiah.replace(" ", "");
        return Integer.parseInt(rupiah);
    }

	public static double convertRupiahToDouble(String rupiah) {
		rupiah = rupiah.replace("Rp", "");
		rupiah = rupiah.replace(".", "");
		rupiah = rupiah.replace(" ", "");
		rupiah = rupiah.replace(",", ".");
		return Double.parseDouble(rupiah);
	}

    public static String toRupiah(long money) {
		try{
			String inRupiah = getRupiahFormat().format(money).replace(",", ".");
			return inRupiah;
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return "Exception raised";
		}
	}

	public static NumberFormat getRupiahFormat() {
		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getCurrencyInstance();
		DecimalFormatSymbols rupiahFormat = new DecimalFormatSymbols();

		rupiahFormat.setCurrencySymbol("Rp ");
		rupiahFormat.setGroupingSeparator('.');
		formatter.setDecimalFormatSymbols(rupiahFormat);
		formatter.setMaximumFractionDigits(0);
		formatter.setGroupingUsed(true);

		return formatter;
	}
}
