import { NetworkModule, SessionModule } from 'NativeModules'
import { 
  BASE_API_URL_PAYMENT,
  BASE_API_URL_SCROOGE,
  BASE_API_URL_PCIDSS
} from '../lib/api.js'

// ==================== Session Data ===================== //
const getUserId = () => {
  return SessionModule.getUserId()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getAddrId = () => {
  return SessionModule.getAddrId()
    .then(res => { return res })
    .catch(err => console.log(err))
}


// ==================== Session Data ===================== //


//  ==================== Make Payment V2 Native ===================== //
export const MAKE_PAYMENT_V2 = 'MAKE_PAYMENT_V2'
export const makePayment = (checkout_data, installment_term, cc_no, expiry_date, cvv) => {
  const data = { checkout_data, installment_term, cc_no, expiry_date, cvv }
  console.log(data)

  return {
    type: MAKE_PAYMENT_V2,
    payload: postDataToNative(data)
  }
}

const postDataToNative = async (data) => {
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const gateway_code = await getGatewayCode(data)
  const otp = await makePaymentV2(api_url, data, gateway_code)
  console.log(otp)
  return otp
}

const getEnv = () => {
  return SessionModule.getEnv()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getBaseAPI = (env) => {
  let data_api = {}

  if (env === 'production'){
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.PRODUCTION}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.PRODUCTION}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.PRODUCTION}`,
      api_url_order: `${BASE_API_URL_ORDER.PRODUCTION}`
    }
    return data_api

  } else {
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.STAGING}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.STAGING}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.STAGING}`,
      api_url_order: `${BASE_API_URL_ORDER.STAGING}`
    }
    return data_api
  }
}

const getGatewayCode = (data) => {
  let gtwCode = ''
  if (data.installment_term > 0 ){
    gtwCode = 'INSTALLMENT'
  } else {
    gtwCode = 'CREDITCARD'
  }
  return gtwCode
}

const makePaymentV2 = (api_url, data, gateway_code) => {
  const checkout_data = data.checkout_data
  const exp_month = (data.expiry_date).substring(0, (data.expiry_date).indexOf('/'))
  const exp_year = (data.expiry_date).substring((data.expiry_date).indexOf("/") + 1)
  const cc_card_no_without_spaces = (data.cc_no).replace(/\s/g, '')

  const data_params = {
    cc_card_no: cc_card_no_without_spaces,
    cc_expired_month: parseInt(exp_month),
    cc_expired_year: parseInt(exp_year),
    cc_cvv: parseInt(data.cvv),
    inst_term: data.installment_term,
    gateway_code: gateway_code,
    payment_amount: parseFloat(checkout_data.data.payment_amount),
    merchant_code: checkout_data.data.merchant_code,
    profile_code: checkout_data.data.profile_code,
    transaction_id: checkout_data.data.transaction_id,
    signature: checkout_data.data.signature
  }
  console.log(data_params)

  return NetworkModule.getResponse(`${api_url.api_url_pcidss}/v2/payment/process/CREDITCARD`, `POST`, JSON.stringify(data_params), true)
    .then(res => {
      const jsonResponse = JSON.parse(res)
      console.log(jsonResponse)
      return jsonResponse
    })
    .catch(err => { 
      console.log(err) 
      return
    })
}
//  ==================== Make Payment V2 Native ===================== //


//  ==================== Transaction History ===================== //
export const FETCH_TRANSACTION_HISTORY = 'FETCH_TRANSACTION_HISTORY'
export const getTransactionHistory = () => {
  return {
    type: FETCH_TRANSACTION_HISTORY,
    payload: fetchTxHistory()
  }
}

const fetchTxHistory = async () => {
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const user_id = await getUserId()
  const addr_id = await getAddrId()
  // need to work on pagination
  const data = {
    user_id,
    addr_id,
    per_page: 10,
    page: 1,
    os_type: '1'
  }
  console.log(data)
  const txHistory = await apiGetTxHistory(`${api_url.api_url_order}/api/order/i/v1/o2o/get_order_list_details`, data)
  return txHistory
}

const apiGetTxHistory = (url, data) => {
  return NetworkModule.getResponseJson(url, `POST`, JSON.stringify(data), false)
    .then(res => {
        const jsonResponse = JSON.parse(res)
        console.log(jsonResponse)
        return jsonResponse
    })
    .catch(err => {
      console.log(err)
      return
    })
}
//  ==================== Transaction History ===================== //






