import { PosCacheModule } from 'NativeModules'


export const BANK_SELECTED = 'BANK_SELECTED'
export const selectBank = (id) => {
  return {
    type: BANK_SELECTED,
    payload: id
  }
}

//  ==================== Fetch data Bank from Cache ===================== //
export const FETCH_BANK_FUlFILLED = 'FETCH_BANK_FUlFILLED'
export const getBankList = () => {
  return {
    type: FETCH_BANK_FUlFILLED,
    payload: fetchBankData()
  }
}

const fetchBankData = () => {
  return PosCacheModule.getDataAll('BANK')
    .then(response => {
      const jsonResponse = JSON.parse(response)
      if (jsonResponse.data) return jsonResponse.data.list
    })
    .catch(err => console.log(err))
}


//  ==================== Fetch data Installment from Cache ===================== //
export const FETCH_EMI_FUlFILLED = 'FETCH_EMI_FUlFILLED'
export const getEmiList = () => {
  return {
    type: FETCH_EMI_FUlFILLED,
    payload: fetchBankData()
  }
}

export const SELECT_PAYMENT_OPTIONS = 'SELECT_PAYMENT_OPTIONS'
export const selectPaymentOptions = (option, value) => {
  return {
    type: SELECT_PAYMENT_OPTIONS,
    payload: { option: option, value: value }
  }
}



export const EMI_SELECTED = 'EMI_SELECTED'
export const selectEmi = (id) => {
  return {
    type: EMI_SELECTED,
    payload: id
  }
}