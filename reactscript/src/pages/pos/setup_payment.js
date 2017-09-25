import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
import PaymentBank from './components/PaymentBank'
import Payment from './components/Payment'
import PaymentProcessing from './components/PaymentProcessing'
import PaymentInvoice from './components/PaymentInvoice'
import TransactionHistory from './components/TransactionHistory'
import PasswordPopup from './components/PasswordPopup'
import POS from './components/POS'
import CartContainer from './containers/CartContainer'
import { StackNavigator } from 'react-navigation';

const prefix = 'pospayment://pospayment/'


// // TODO: FOR UI testing purpose
const PaymentApp = StackNavigator({
    BankSelection : { screen: PaymentBank },
    Payment: { screen: Payment },
    // PaymentProcessing: {
    //   screen: PaymentProcessing,
    //   path: 'PaymentProcessing/:data',
    // },
    // PaymentInvoice: {screen: PaymentInvoice},
    // TransactionHistory: {screen: TransactionHistory},
    // PasswordPopup: {screen: PasswordPopup}
});
// const PaymentApp = StackNavigator({
//     BankSelection : { screen: PaymentBank },
//     Payment: { screen: Payment },
//     // PaymentProcessing: {screen: PaymentProcessing},
//     PaymentProcessing: {
//       screen: PaymentProcessing,
//       path: 'PaymentProcessing',
//     },
//     PaymentInvoice: {screen: PaymentInvoice},
//     TransactionHistory: {screen: TransactionHistory},
//     PasswordPopup: {screen: PasswordPopup}
// }, {headerMode: 'none'});


class Root extends Component {
    componentWillMount(){
        console.log(this.props)
    }

    render() {
        return (
            <Provider store={store}>
                <PaymentApp screenProps={this.props.data} />
            </Provider>
        )
  }
}
export default Root