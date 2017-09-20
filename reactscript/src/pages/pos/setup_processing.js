import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
// import PaymentBank from './components/PaymentBank'
// import Payment from './components/Payment'
import PaymentProcessing from './components/PaymentProcessing'
import PaymentInvoice from './components/PaymentInvoice'
// import TransactionHistory from './components/TransactionHistory'
// import PasswordPopup from './components/PasswordPopup'
// import POS from './components/POS'
// import CartContainer from './containers/CartContainer'
import { StackNavigator } from 'react-navigation';
// import Setup_processing from './wyz/setup_processing'
// import Setup_invoice from './wyz/setup_paymentinvoice'



const App = StackNavigator({
    PaymentProcessing: {
        screen: PaymentProcessing,
        path: 'PaymentProcessing',
      },
    PaymentInvoice: {screen: PaymentInvoice}
}, { headerMode: 'none' });

const prefix = 'pospayment://pospayment/'


class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                <App uriPrefix={prefix} />
            </Provider>
        )
    }
}
export default Root

