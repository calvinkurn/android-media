import { NetworkModule, PosCacheModule, SessionModule } from 'NativeModules'
import { 
  getUserId, 
  getAddrId,
  getShopId,
  getBaseAPI,
  getEnv,
} from '../lib/utility'



// ===================== Retrieve Payment Rate =================== //
export const GET_PAYMENT_RATE = 'GET_PAYMENT_RATE'
export const GetPaymentRate = () => {
  return {
    type: GET_PAYMENT_RATE,
    payload: get_PaymentRate()
  }
}

const get_PaymentRate = async () => {
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const payment_rate = await PaymentRate(api_url)
  console.log(payment_rate)
  return payment_rate
}

const PaymentRate = async (api_url) => {
  const url= `${api_url.base_api_url}o2o/v1/payment/get_payment_interest`
  return NetworkModule.getResponse(`${url}`, `GET`, '', true)
    .then(res => {
      const jsonResponse = JSON.parse(res)
      if (jsonResponse.status === '200 Ok' && jsonResponse.error_message.length === 0){
        return jsonResponse
      }
    })
    .catch(err => {
      console.log(res)
    })
}


// ===================== Make Payment ===================== //
export const PAYMENT_CHECKOUT_TO_NATIVE = 'PAYMENT_CHECKOUT_TO_NATIVE'
export const PaymentCheckoutToNative = () => {
  return {
    type: PAYMENT_CHECKOUT_TO_NATIVE,
    payload: doPayment()
  }
}

const doPayment = async () => {
  const user_id = await getUserId()
  const addr_id = await getAddrId()
  const shop_id = await getShopId()
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const local_cart = await getCart()
  
  const data_payment = {
    base_api_url_payment: api_url.api_url_payment,
    base_api_url_scrooge: api_url.api_url_scrooge,
    base_api_url_pcidss: api_url.api_url_pcidss,
    user_id: parseInt(user_id),
    os_type: '1',
    addr_id: parseInt(addr_id),
    client_id: '',
    shop_id: parseInt(shop_id),
    cart: local_cart
  }
  const paymentToNative_getParams = await makePaymentToNativeStepOne(data_payment)
  const paymentToNative_secondStep = await makePaymentToNativeStepTwo(paymentToNative_getParams, data_payment, local_cart)
  if(paymentToNative_secondStep) return paymentToNative_secondStep
  else throw 'Checkout Error'
}


const getCart = async () => {
  let objData = []
  const cart = await fetchCart()
  
  cart.list.map((data) => {
    objData.push({
      product_id: data.product_id,
      quantity: data.quantity
    })
  })
  return objData
}

const fetchCart = () => {
  return PosCacheModule.getDataAll('CART')
    .then(response => {
      const jsonResponse = JSON.parse(response)
      return jsonResponse.data;
    })
    .catch(error => console.log(error))
}




const makePaymentToNativeStepOne = (data_payment) => {
  const data = {
    user_id: data_payment.user_id,
    os_type: data_payment.os_type,
    addr_id: data_payment.addr_id,
    client_id: data_payment.client_id,
    shop_id: data_payment.shop_id,
    cart: data_payment.cart
  }

  return NetworkModule.getResponseJson(`${data_payment.base_api_url_payment}`, `POST`, JSON.stringify(data), true)
    .then(response => {
      const jsonResponse = JSON.parse(response)
      console.log(jsonResponse)
      if (jsonResponse.data.status !== 'ERROR'){
        const itemsRes = jsonResponse.data.data.parameter.items
        let itemsData = []
  
        itemsRes.map((res) => {
          itemsData.push({ 
            id: res.id,
            name: res.name,
            price: res.price,
            quantity: res.quantity
          })
        })
        return { jsonResponse, itemsData}
      }
    })
    .catch(err => { 
      console.log(err) 
      return
    })
}
// ===================== Make Payment ===================== //


// ===================== Make Payment 2 ===================== //
const makePaymentToNativeStepTwo = (paymentToNative_getParams, data_payment, local_cart) => {
  let data_items = []
  const paymentParams = paymentToNative_getParams.itemsData

  if (!paymentParams) return 

  const data_cart = paymentToNative_getParams.jsonResponse.data.data.parameter.items || []
  data_cart.map((res) => {
    data_items.push({
      Name: res.name,
      Quantity: res.quantity,
      Price: res.price
    })
  })
  
  const dataParams = paymentToNative_getParams.jsonResponse.data.data.parameter

  const payloads = {
    merchant_code: dataParams.merchant_code,
    profile_code: dataParams.profile_code,
    transaction_id: dataParams.transaction_id,
    transaction_date: dataParams.transaction_date,
    currency: dataParams.currency,
    amount: parseFloat(dataParams.amount),
    customer_name: dataParams.customer_name,
    customer_email: dataParams.customer_email,
    user_defined_value: dataParams.user_defined_value,
    payment_metadata: dataParams.payment_meta,
    signature: dataParams.signature,
    items: data_items
  }
  const payloadString = JSON.stringify(payloads)
  
  return NetworkModule.getResponseJson(`${data_payment.base_api_url_scrooge}`, `POST`, payloadString, true)
    .then(res => {
      const response = JSON.parse(res)
      const jsonResponse =  {
        ...response,
        payment_param: payloads
      }
      if (jsonResponse.status === '200 Ok'){
        if (jsonResponse.data.code === 200 && jsonResponse.data.message === 'Success'){
          // insertPaymentParams(dataParams)
          return jsonResponse
        }
      }
    })
    .catch(err => {
      console.log(err)
      return 
    })
}
// ===================== Make Payment 2 ===================== //


// ========== Insert to Cache Payment Params ============ //
const insertPaymentParams = (dataPaymentParams) => {
  console.log(dataPaymentParams)

  const data_params = {
    key: `PAYMENTPARAMS_${dataParams.transaction_id}`,
    data: dataPaymentParams
  }

  PosCacheModule.insert('GLOBAL', JSON.stringify(data_params))
    .then(res => {
      console.log(res)
      return 
    })
    .catch(err => {
      console.log(err)
    })
}