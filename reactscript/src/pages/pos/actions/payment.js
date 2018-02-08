import { NetworkModule, SessionModule } from 'NativeModules'
import { 
  getUserId, 
  getAddrId,
  getShopId,
  getBaseAPI,
  getEnv,
  getAddrName,
  getShopName,
} from '../lib/utility'



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
  // console.log(otp)
  return otp
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
  const checkoutData = data.checkout_data.data.data
  const exp_month = (data.expiry_date).substring(0, (data.expiry_date).indexOf('/'))
  const exp_year = (data.expiry_date).substring((data.expiry_date).indexOf("/") + 1)
  const cc_card_no_without_spaces = (data.cc_no).replace(/\s/g, '')

  const data_params = {
    cc_card_no: cc_card_no_without_spaces,
    cc_expired_month: parseInt(exp_month),
    cc_expired_year: parseInt(exp_year),
    cc_cvv: data.cvv,
    inst_term: data.installment_term,
    gateway_code: gateway_code,
    payment_amount: parseFloat(checkoutData.payment_amount),
    merchant_code: checkoutData.merchant_code,
    profile_code: checkoutData.profile_code,
    transaction_id: checkoutData.transaction_id,
    signature: checkoutData.signature
  }

  return NetworkModule.getResponseJson(`${api_url.api_url_pcidss}`, `POST`, JSON.stringify(data_params), true)
    .then(res => {
      const jsonResponse = JSON.parse(res)
      return jsonResponse
      // if (jsonResponse.status === '200 Ok'){
      //   if (jsonResponse.data.errors === null){
      //     // insertPayment_v2(data, data_params.transaction_id)
      //     return jsonResponse
      //   }
      // }
    })
    .catch(err => { 
      console.log(err) 
      return
    })
}
//  ==================== Make Payment V2 Native ===================== //



// ========== Insert to Cache Payment V2 Params ============ //
const insertPayment_v2 = (data, tansaction_id) => {
  console.log(data)
  const data_params = {
    key: `PAYMENTV2_${tansaction_id}`,
    data: data
  }

  PosCacheModule.insert('GLOBAL', JSON.stringify(data_params))
    .then(res => {
      console.log(res)
      return 
    })
    .catch(err => {
      console.log(err)
      return
    })
}
// ========== Insert to Cache Payment Params ============ //







//  ==================== Transaction History ===================== //
export const FETCH_TRANSACTION_HISTORY = 'FETCH_TRANSACTION_HISTORY'
export const getTransactionHistory = (page) => {
  let page_api = page + 1
  return {
    type: FETCH_TRANSACTION_HISTORY,
    payload: fetchTransactionHistory(page_api)
  }
}

const fetchTransactionHistory = async (page) => {
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const user_id = await getUserId()
  const addr_id = await getAddrId()
  const outlet_name = await getAddrName()
  const shop_name = await getShopName()

  const data = {
    user_id: parseInt(user_id),
    addr_id: parseInt(addr_id),
    per_page: 10,
    page: page,
    os_type: '1'
  }
  const txHistory = await apiGetTransactionHistory(`${api_url.base_api_url}o2o/v1/order/get_history`, data)
  const data_response = { ...txHistory, page, outlet_name, shop_name }
  return data_response
}

const apiGetTransactionHistory = (url, data) => {
  return NetworkModule.getResponseJson(url, `POST`, JSON.stringify(data), true)
    .then(res => {
        const jsonResponse = JSON.parse(res)
        return jsonResponse
    })
    .catch(err => {
      console.log(err)
      return
    })
}
//  ==================== Transaction History ===================== //






